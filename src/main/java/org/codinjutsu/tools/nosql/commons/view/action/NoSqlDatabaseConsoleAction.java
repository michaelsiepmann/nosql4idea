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

package org.codinjutsu.tools.nosql.commons.view.action;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.DatabaseVendorInformation;
import org.codinjutsu.tools.nosql.NoSqlConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.console.AbstractNoSQLConsoleRunner;
import org.codinjutsu.tools.nosql.commons.view.explorer.DatabaseListPanel;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.codinjutsu.tools.nosql.DatabaseVendor.MONGO;
import static org.codinjutsu.tools.nosql.DatabaseVendor.REDIS;

public class NoSqlDatabaseConsoleAction extends AnAction implements DumbAware {

    private final DatabaseListPanel databaseListPanel;

    public NoSqlDatabaseConsoleAction(DatabaseListPanel databaseListPanel) {
        super("DB Shell...", "DB Shell", GuiUtils.loadIcon("toolConsole.png")); //NON-NLS
        this.databaseListPanel = databaseListPanel;
    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getData(PlatformDataKeys.PROJECT);

        if (project == null) {
            return;
        }

        NoSqlConfiguration configuration = NoSqlConfiguration.getInstance(project);

        Presentation presentation = e.getPresentation();
        presentation.setVisible(
                (configuration != null) &&
                        (isNotBlank(configuration.getShellPath(MONGO)) ||
                                isNotBlank(configuration.getShellPath(REDIS))
                        ) &&
                        (databaseListPanel.getConfiguration() != null) &&
                        databaseListPanel.getConfiguration().isSingleServer()
        );
        presentation.setEnabled(databaseListPanel.hasDatabaseConsoleApplication());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        assert project != null;
        runShell(project);
    }

    private void runShell(Project project) {
        ServerConfiguration configuration = databaseListPanel.getConfiguration();
        DatabaseVendorInformation databaseVendorInformation = configuration.getDatabaseVendorInformation();
        if (databaseVendorInformation.getHasConsoleWindow()) {
            AbstractNoSQLConsoleRunner consoleRunner = databaseVendorInformation.createConsoleRunner(project, configuration, databaseListPanel.getSelectedDatabase());
            try {
                if (consoleRunner != null) {
                    consoleRunner.initAndRun();
                }
            } catch (ExecutionException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
