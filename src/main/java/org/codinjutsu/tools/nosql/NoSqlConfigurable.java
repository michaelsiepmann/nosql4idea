/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.PanelWithButtons;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.apache.commons.lang3.StringUtils;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.view.ServerConfigurationPanelFactory;
import org.codinjutsu.tools.nosql.i18n.ResourcesLoaderKt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.codinjutsu.tools.nosql.DatabaseVendor.MONGO;
import static org.codinjutsu.tools.nosql.DatabaseVendor.REDIS;
import static org.codinjutsu.tools.nosql.DatabaseVendor.SOLR;
import static org.codinjutsu.tools.nosql.i18n.ResourcesLoaderKt.getResourceString;

public class NoSqlConfigurable extends BaseConfigurable {

    private final Project project;

    private final NoSqlConfiguration configuration;

    private final List<ServerConfiguration> configurations;
    private final ServerConfigurationPanelFactory serverConfigurationPanelFactory;

    private JPanel mainPanel;
    private JBTable table;
    private final NoSqlServerTableModel tableModel;
    private ShellPathPanel mongoShellPanel;
    private ShellPathPanel redisShellPanel;
    private ShellPathPanel solrShellPanel;

    public NoSqlConfigurable(Project project) {
        this.project = project;
        configuration = NoSqlConfiguration.getInstance(project);
        serverConfigurationPanelFactory = new ServerConfigurationPanelFactory(
                project,
                DatabaseVendorUIManager.getInstance(project)
        );
        configurations = new LinkedList<>(configuration.getServerConfigurations());
        tableModel = new NoSqlServerTableModel(configurations);
        mainPanel = new JPanel(new BorderLayout());
    }

    @Nls
    @Override
    public String getDisplayName() {
        return getResourceString("settings.displayname");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preferences.noSqlOptions";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel databaseVendorShellOptionsPanel = new JPanel();
        databaseVendorShellOptionsPanel.setLayout(new BoxLayout(databaseVendorShellOptionsPanel, BoxLayout.Y_AXIS));
        mongoShellPanel = new ShellPathPanel(MONGO, "--version"); //NON-NLS
        databaseVendorShellOptionsPanel.add(mongoShellPanel);
        redisShellPanel = new ShellPathPanel(REDIS, "--version"); //NON-NLS
        databaseVendorShellOptionsPanel.add(redisShellPanel);
        solrShellPanel = new ShellPathPanel(SOLR, "-V"); //NON-NLS
        databaseVendorShellOptionsPanel.add(solrShellPanel);

        mainPanel.add(databaseVendorShellOptionsPanel, NORTH);

        mainPanel.add(new TablePanel(), CENTER);

        return mainPanel;
    }

    public boolean isModified() {
        return areConfigurationsModified() || isRedisShellPathModified();
    }

    @Override
    public void apply() {
        stopEditing();
        if (areConfigurationsModified()) {
            configuration.setServerConfigurations(configurations);
        }

        if (isMongoShellPathModified()) {
            configuration.setShellPath(MONGO, mongoShellPanel.getShellPath());
        }

        if (isRedisShellPathModified()) {
            configuration.setShellPath(REDIS, redisShellPanel.getShellPath());
        }

        if (isSolrShellPathModified()) {
            configuration.setShellPath(SOLR, solrShellPanel.getShellPath());
        }

        ServiceManager.getService(project, NoSqlWindowManager.class).apply();
    }

    private boolean isMongoShellPathModified() {
        return mongoShellPanel.isShellPathModified(NoSqlConfiguration.getInstance(project).getShellPath(MONGO));
    }

    private boolean isRedisShellPathModified() {
        return redisShellPanel.isShellPathModified(NoSqlConfiguration.getInstance(project).getShellPath(REDIS));
    }

    private boolean isSolrShellPathModified() {
        return solrShellPanel.isShellPathModified(NoSqlConfiguration.getInstance(project).getShellPath(SOLR));
    }

    private boolean areConfigurationsModified() {
        List<ServerConfiguration> existingConfigurations = NoSqlConfiguration.getInstance(project).getServerConfigurations();
        return configurations.size() != existingConfigurations.size() || existingConfigurations.stream().anyMatch(existingConfiguration -> !configurations.contains(existingConfiguration));
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
        mainPanel = null;
        tableModel.removeTableModelListener(table);
        mongoShellPanel.dispose();
        redisShellPanel.dispose();
        solrShellPanel.dispose();
        table = null;
    }

    private void stopEditing() {
        if (table.isEditing()) {
            TableCellEditor editor = table.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }

    private void onAddAction() {
        stopEditing();

        SelectDatabaseVendorDialog databaseVendorDialog = new SelectDatabaseVendorDialog(mainPanel);
        databaseVendorDialog.setTitle(getResourceString("settings.dialog.selectdatabase.title"));
        databaseVendorDialog.show();
        if (!databaseVendorDialog.isOK()) {
            return;
        }

        DatabaseVendorInformation databaseVendorInformation = databaseVendorDialog.getSelectedDatabaseVendor().getDatabaseVendorInformation();
        DatabaseClient client = databaseVendorInformation.getClient(project);
        if (client == null) {
            return;
        }
        WriteableServerConfiguration serverConfiguration = (WriteableServerConfiguration) client.defaultConfiguration();

        ConfigurationDialog dialog = new ConfigurationDialog(
                mainPanel,
                serverConfigurationPanelFactory,
                serverConfiguration
        );
        dialog.setTitle(getResourceString("settings.dialog.configuredatabase.add.title"));
        dialog.show();
        if (dialog.isOK()) {
            configurations.add(serverConfiguration);
            int index = configurations.size() - 1;
            tableModel.fireTableRowsInserted(index, index);
            table.getSelectionModel().setSelectionInterval(index, index);
            table.scrollRectToVisible(table.getCellRect(index, 0, true));
        }
    }

    private void onEditAction() {
        stopEditing();

        int selectedIndex = table.getSelectedRow();
        if (selectedIndex < 0 || selectedIndex >= tableModel.getRowCount()) {
            return;
        }
        ServerConfiguration sourceConfiguration = configurations.get(selectedIndex);
        WriteableServerConfiguration copiedConfiguration = (WriteableServerConfiguration) sourceConfiguration.copy();

        ConfigurationDialog dialog = new ConfigurationDialog(
                mainPanel,
                serverConfigurationPanelFactory,
                copiedConfiguration
        );
        dialog.setTitle(getResourceString("settings.dialog.configuredatabase.edit.title"));
        dialog.show();
        if (dialog.isOK()) {
            configurations.set(selectedIndex, copiedConfiguration);
            tableModel.fireTableRowsUpdated(selectedIndex, selectedIndex);
            table.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
        }
    }

    private void onRemoveAction() {
        stopEditing();
        int selectedIndex = table.getSelectedRow();
        if (selectedIndex >= 0 && selectedIndex < tableModel.getRowCount()) {
            TableUtil.removeSelectedItems(table);
        }
    }

    private class ShellPathPanel extends JPanel implements Disposable {

        private static final int TIMEOUT_MS = 60 * 1000;
        private final String testParameter;

        private LabeledComponent<TextFieldWithBrowseButton> shellPathField;

        private ShellPathPanel(DatabaseVendor databaseVendor, String testParameter) {
            this.testParameter = testParameter;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            String vendorName = databaseVendor.getName();
            add(createLabel(vendorName));
            shellPathField = createShellPathField(databaseVendor);
            add(shellPathField);
            add(createTestButton(vendorName));
        }

        private JLabel createLabel(String databaseVendorName) {
            return new JLabel(getResourceString("settings.pathtocli.label", databaseVendorName));
        }

        private JButton createTestButton(String databaseVendorName) {
            JButton testButton = new JButton(getResourceString("settings.testbutton.label"));
            testButton.addActionListener(actionEvent -> testPath(databaseVendorName));
            return testButton;
        }

        private void testPath(String databaseVendorName) {
            ProcessOutput processOutput;
            try {
                processOutput = ProgressManager.getInstance()
                        .runProcessWithProgressSynchronously(() -> checkShellPath(databaseVendorName, getShellPath()), getResourceString("settings.testing.progresstitle", databaseVendorName), true, project);
            } catch (ProcessCanceledException pce) {
                return;
            } catch (Exception e) {
                Messages.showErrorDialog(mainPanel, e.getMessage(), getResourceString("settings.errordialog.title"));
                return;
            }
            if (processOutput != null && processOutput.getExitCode() == 0) {
                Messages.showInfoMessage(mainPanel, processOutput.getStdout(), getResourceString("settings.clipath.confirmed", databaseVendorName));
            }
        }

        ProcessOutput checkShellPath(String databaseVendorName, String shellPath) throws ExecutionException, TimeoutException {
            if (isBlank(shellPath)) {
                return null;
            }

            GeneralCommandLine commandLine = new GeneralCommandLine();
            commandLine.setExePath(shellPath);
            if (testParameter != null) {
                commandLine.addParameter(testParameter);
            }
            CapturingProcessHandler handler = new CapturingProcessHandler(commandLine.createProcess(), CharsetToolkit.getDefaultSystemCharset(), "");
            ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
            ProcessOutput result = indicator == null ?
                    handler.runProcess(TIMEOUT_MS) :
                    handler.runProcessWithProgressIndicator(indicator);
            if (result.isTimeout()) {
                throw new TimeoutException(getResourceString("settings.errormessages.clitimeout", databaseVendorName));
            }
            if (result.isCancelled()) {
                throw new ProcessCanceledException();
            }
            if (result.getExitCode() != 0 || !result.getStderr().isEmpty()) {
                throw new ExecutionException(getResourceString("settings.errormessages.generalerror", commandLine.toString(), result.getExitCode(), result.getStderr()));
            }
            return result;
        }

        String getShellPath() {
            String shellPath = shellPathField.getComponent().getText();
            if (StringUtils.isNotBlank(shellPath)) {
                return shellPath;
            }

            return null;
        }

        boolean isShellPathModified(String shellPath) {
            return !StringUtils.equals(shellPath, getShellPath());
        }

        private LabeledComponent<TextFieldWithBrowseButton> createShellPathField(DatabaseVendor databaseVendor) {
            LabeledComponent<TextFieldWithBrowseButton> shellPathField = new LabeledComponent<>();
            TextFieldWithBrowseButton component = new TextFieldWithBrowseButton();
            component.getChildComponent().setName("shellPathField"); //NON-NLS
            shellPathField.setComponent(component);
            shellPathField.getComponent().addBrowseFolderListener(getResourceString("settings.cliconfigbrowserdialog.title", databaseVendor.getName()), "", null,
                    new FileChooserDescriptor(true, false, false, false, false, false));

            shellPathField.getComponent().setText(configuration.getShellPath(databaseVendor));

            return shellPathField;
        }

        @Override
        public void dispose() {
            shellPathField = null;
        }

    }

    private class TablePanel extends PanelWithButtons {

        private TablePanel() {
            initPanel();
        }

        @NotNull
        @Override
        protected String getLabelText() {
            return getResourceString("settings.servers.label");
        }

        @Override
        protected JButton[] createButtons() {
            return new JButton[]{};
        }

        @Override
        protected JComponent createMainComponent() {
            table = new JBTable(tableModel);
            table.getEmptyText().setText(ResourcesLoaderKt.getResourceString("settings.message.noserverconfiguration"));
            table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setDefaultRenderer(DatabaseVendor.class, new ColoredTableCellRenderer() {
                @Override
                protected void customizeCellRenderer(JTable jTable, Object value, boolean b, boolean b1, int i, int i1) {
                    DatabaseVendor databaseVendor = (DatabaseVendor) value;
                    setIcon(databaseVendor.getIcon());
                    append(databaseVendor.getName());
                }
            });

            TableColumn autoConnectColumn = table.getColumnModel().getColumn(3);
            int autoConnectColumnWidth = table.getFontMetrics(table.getFont()).stringWidth(table.getColumnName(3)) + 10;
            autoConnectColumn.setPreferredWidth(autoConnectColumnWidth);
            autoConnectColumn.setMaxWidth(autoConnectColumnWidth);
            autoConnectColumn.setMinWidth(autoConnectColumnWidth);

            return ToolbarDecorator.createDecorator(table)
                    .setAddAction(button -> onAddAction())
                    .setAddActionName("addServer") //NON-NLS
                    .setEditAction(button -> onEditAction())
                    .setEditActionName("editServer") //NON-NLS
                    .setRemoveAction(button -> onRemoveAction())
                    .setRemoveActionName("removeServer") //NON-NLS
                    .disableUpDownActions()
                    .createPanel();
        }
    }
}
