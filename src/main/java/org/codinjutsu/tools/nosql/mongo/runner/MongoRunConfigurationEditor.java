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

package org.codinjutsu.tools.nosql.mongo.runner;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.RawCommandLineEditor;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.mongo.configuration.MongoServerConfiguration;
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Collection;
import java.util.Vector;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MongoRunConfigurationEditor extends SettingsEditor<MongoRunConfiguration> {

    private JPanel mainPanel;

    private JTextField scriptPathField;
    private ComboBox serverConfigurationCombobox;
    private ComboBox databaseCombobox;
    private JPanel mongoShellOptionsPanel;
    private RawCommandLineEditor shellParametersField;
    private TextFieldWithBrowseButton shellWorkingDirField;


    public MongoRunConfigurationEditor(Project project) {
        mongoShellOptionsPanel.setBorder(IdeBorderFactory.createTitledBorder("Mongo shell options", true));

        Collection<DatabaseServer> mongoServers = MongoClient.getInstance(project).filterAvailableServers();

        if (mongoServers.isEmpty()) {
            serverConfigurationCombobox.setEnabled(false);
            databaseCombobox.setEnabled(false);
            return;
        }

        serverConfigurationCombobox.setModel(new DefaultComboBoxModel<>(new Vector<>(mongoServers)));

        serverConfigurationCombobox.setRenderer(new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
                DatabaseServer serverConfiguration = (DatabaseServer) value;
                append(serverConfiguration.getLabel());
            }
        });


        databaseCombobox.setRenderer(new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(JList list, Object value, int index, boolean selected, boolean hasFocus) {
                MongoDatabase mongoDatabase = (MongoDatabase) value;
                if (value == null) {
                    return;
                }
                append(mongoDatabase.getName());
            }
        });


        serverConfigurationCombobox.addItemListener(itemEvent -> {
            DatabaseServer selectedServer = (DatabaseServer) serverConfigurationCombobox.getSelectedItem();
            if (selectedServer == null) {
                return;
            }
            databaseCombobox.removeAllItems();
            for (Database mongoDatabase : selectedServer.getDatabases()) {
                databaseCombobox.addItem(mongoDatabase);
            }
        });

        serverConfigurationCombobox.setSelectedIndex(-1);
        serverConfigurationCombobox.setSelectedIndex(0);
    }

    @Override
    protected void resetEditorFrom(@NotNull MongoRunConfiguration configuration) {
        scriptPathField.setText(configuration.getScriptPath() != null ? configuration.getScriptPath().getPath() : null);
        shellParametersField.setText(configuration.getShellParameters());
        shellWorkingDirField.setText(configuration.getShellWorkingDir());
    }

    @Override
    protected void applyEditorTo(@NotNull MongoRunConfiguration configuration) {
        configuration.setScriptPath(getScriptPath());
        configuration.setServerConfiguration(getSelectedConfiguration());
        configuration.setDatabase(getSelectedDatabase());
        configuration.setShellParameters(getShellParameters());
        configuration.setShellWorkingDir(getShellWorkingDir());
    }

    private String getScriptPath() {
        return scriptPathField.getText();
    }

    private String getShellParameters() {
        return shellParametersField.getText();
    }

    private MongoServerConfiguration getSelectedConfiguration() {
        DatabaseServer selectedServer = (DatabaseServer) serverConfigurationCombobox.getSelectedItem();
        return selectedServer == null ? null : (MongoServerConfiguration) selectedServer.getConfiguration();
    }

    public MongoDatabase getSelectedDatabase() {
        return (MongoDatabase) databaseCombobox.getSelectedItem();
    }

    private String getShellWorkingDir() {
        String shellWorkingDir = shellWorkingDirField.getText();
        if (isNotBlank(shellWorkingDir)) {
            return shellWorkingDir;
        }

        return null;
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    @Override
    protected void disposeEditor() {

    }

    private void createUIComponents() {
        shellWorkingDirField = new TextFieldWithBrowseButton();
        shellWorkingDirField.addBrowseFolderListener("Mongo shell working directory", "", null,
                new FileChooserDescriptor(false, true, false, false, false, false));
        shellWorkingDirField.setName("shellWorkingDirField"); //NON-NLS
    }
}
