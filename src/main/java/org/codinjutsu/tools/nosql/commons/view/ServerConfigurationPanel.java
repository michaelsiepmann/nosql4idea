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

package org.codinjutsu.tools.nosql.commons.view;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Ref;
import com.intellij.ui.RawCommandLineEditor;
import org.codinjutsu.tools.nosql.DatabaseVendor;
import org.codinjutsu.tools.nosql.commons.configuration.ConsoleRunnerConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.WriteableConsoleRunnerConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.view.authentication.AuthenticationView;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;

import static com.intellij.ui.IdeBorderFactory.createTitledBorder;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.codinjutsu.tools.nosql.i18n.ResourcesLoaderKt.getResourceString;

public class ServerConfigurationPanel extends JPanel {

    private JPanel rootPanel;
    private JTextField labelField;
    private JPanel authenticationContainer;
    private JTextField serverUrlField;
    private JTextField userDatabaseField;
    private JCheckBox autoConnectCheckBox;
    private JButton testConnectionButton;
    private JPanel shellOptionsPanel;
    private TextFieldWithBrowseButton shellWorkingDirField;
    private RawCommandLineEditor shellArgumentsLineField;
    private JLabel databaseTipsLabel;
    private JLabel databaseVendorLabel;

    private final Project project;

    private final DatabaseClient databaseClient;
    private final AuthenticationView authenticationView;
    private final String databaseVendorName;

    public ServerConfigurationPanel(Project project,
                                    DatabaseVendor databaseVendor,
                                    AuthenticationView authenticationView,
                                    boolean hasOptionsPanel) {
        this.project = project;
        databaseVendorName = databaseVendor.getName();
        this.authenticationView = authenticationView;
        databaseClient = databaseVendor.getDatabaseVendorInformation().getClient(project);

        setLayout(new BorderLayout());
        add(rootPanel, BorderLayout.CENTER);
        authenticationContainer.add(authenticationView.getComponent());

        labelField.setName("labelField"); //NON-NLS
        databaseVendorLabel.setName("databaseVendorLabel"); //NON-NLS
        databaseVendorLabel.setText(databaseVendor.getName());
        databaseVendorLabel.setIcon(databaseVendor.getIcon());

        databaseTipsLabel.setName("databaseTipsLabel"); //NON-NLS
        databaseTipsLabel.setText(databaseVendor.getTips());

        serverUrlField.setName("serverUrlField"); //NON-NLS

        authenticationContainer.setBorder(createTitledBorder(getResourceString("configuration.authentication.title"), true));
        userDatabaseField.setName("userDatabaseField"); //NON-NLS
        userDatabaseField.setToolTipText(getResourceString("configuration.userdatabase.tooltip"));

        autoConnectCheckBox.setName("autoConnectField"); //NON-NLS

        String vendorName = databaseVendor.getVendorName();
        if (hasOptionsPanel) {
            shellOptionsPanel.setBorder(createTitledBorder(getResourceString("configuration.shelloptions.title", vendorName), true));
        } else {
            shellOptionsPanel.setVisible(false);
        }
        testConnectionButton.setName("testConnection"); //NON-NLS

        shellWorkingDirField.setText(null);
        initListeners();
    }

    private void initListeners() {
        testConnectionButton.addActionListener(actionEvent -> {

            final Ref<Exception> excRef = new Ref<>();
            final ProgressManager progressManager = ProgressManager.getInstance();
            progressManager.runProcessWithProgressSynchronously(() -> {
                ServerConfiguration configuration = databaseClient.defaultConfiguration();
                applyConfigurationData((WriteableServerConfiguration) configuration);

                final ProgressIndicator progressIndicator = progressManager.getProgressIndicator();
                if (progressIndicator != null) {
                    progressIndicator.setText(getResourceString("configuration.progress.connecting", configuration.getServerUrl()));
                }
                try {
                    databaseClient.connect(configuration);
                } catch (Exception ex) {
                    excRef.set(ex);
                }
            }, getResourceString("configuration.progress.testingconnection", databaseVendorName), true, project);

            if (!excRef.isNull()) {
                Messages.showErrorDialog(rootPanel, excRef.get().getMessage(), getResourceString("configuration.progress.connection.test.failed.title"));
            } else {
                Messages.showInfoMessage(rootPanel, getResourceString("configuration.progress.connection.test.successful.message", databaseVendorName), getResourceString("configuration.progress.connection.test.successful.title"));
            }
        });
    }

    public void loadConfigurationData(ServerConfiguration configuration) {
        labelField.setText(configuration.getLabel());
        serverUrlField.setText(configuration.getServerUrl());
        userDatabaseField.setText(configuration.getUserDatabase());
        if (configuration instanceof ConsoleRunnerConfiguration) {
            ConsoleRunnerConfiguration consoleRunnerConfiguration = (ConsoleRunnerConfiguration) configuration;
            shellArgumentsLineField.setText(consoleRunnerConfiguration.getShellArgumentsLine());
            shellWorkingDirField.setText(consoleRunnerConfiguration.getShellWorkingDir());
        }
        autoConnectCheckBox.setSelected(configuration.isConnectOnIdeStartup());

        authenticationView.load(configuration.getAuthenticationSettings());
    }

    public void applyConfigurationData(WriteableServerConfiguration configuration) {
        configuration.setLabel(getLabel());
        configuration.setServerUrl(getServerUrls());
        configuration.setAuthenticationSettings(authenticationView.create());
        configuration.setUserDatabase(getUserDatabase());
        if (configuration instanceof WriteableConsoleRunnerConfiguration) {
            WriteableConsoleRunnerConfiguration consoleRunnerConfiguration = (WriteableConsoleRunnerConfiguration) configuration;
            consoleRunnerConfiguration.setShellArgumentsLine(getShellArgumentsLine());
            consoleRunnerConfiguration.setShellWorkingDir(getShellWorkingDir());
        }
        configuration.setConnectOnIdeStartup(isAutoConnect());
    }

    public ValidationInfo validateInputs() {
        if (isEmpty(getLabel())) {
            return new ValidationInfo(getResourceString("configuration.validate.missinglabel"));
        }
        String serverUrl = getServerUrls();
        if (isEmpty(serverUrl)) {
            return new ValidationInfo(getResourceString("configuration.validate.missingurl"));
        }
        return null;
    }

    private String getLabel() {
        String label = labelField.getText();
        if (isNotBlank(label)) {
            return label;
        }
        return null;
    }

    @NotNull
    private String getServerUrls() {
        String serverUrl = serverUrlField.getText();
        if (isNotBlank(serverUrl)) {
            return serverUrl;
        }
        return "";
    }

    private String getUserDatabase() {
        String userDatabase = userDatabaseField.getText();
        if (isNotBlank(userDatabase)) {
            return userDatabase;
        }
        return null;
    }


    private String getShellArgumentsLine() {
        String shellArgumentsLine = shellArgumentsLineField.getText();
        if (isNotBlank(shellArgumentsLine)) {
            return shellArgumentsLine;
        }

        return null;
    }

    private String getShellWorkingDir() {
        String shellWorkingDir = shellWorkingDirField.getText();
        if (isNotBlank(shellWorkingDir)) {
            return shellWorkingDir;
        }

        return null;
    }

    private boolean isAutoConnect() {
        return autoConnectCheckBox.isSelected();
    }

    private void createUIComponents() {
        shellWorkingDirField = new TextFieldWithBrowseButton();
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        shellWorkingDirField.addActionListener(new ComponentWithBrowseButton.BrowseFolderActionListener<>(
                getResourceString("configuration.shell.working.directory.title"),
                null,
                shellWorkingDirField,
                null,
                fileChooserDescriptor,
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT)
        );
        shellWorkingDirField.setName("shellWorkingDirField"); //NON-NLS
    }
}
