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

package org.codinjutsu.tools.nosql.commons.view.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.codinjutsu.tools.nosql.DatabaseVendorInformation;
import org.codinjutsu.tools.nosql.DatabaseVendorUIManager;
import org.codinjutsu.tools.nosql.commons.DatabaseUI;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.fileEditor.FileEditorPolicy.HIDE_DEFAULT_EDITOR;

public class NoSqlDatabaseDataEditorProvider implements FileEditorProvider, DumbAware {

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return DatabaseVendorUIManager.getInstance(project).accept(file);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        DatabaseVendorInformation databaseVendorInformation = ((NoSqlDatabaseObjectFile) file).getConfiguration().getDatabaseVendor().getDatabaseVendorInformation();
        DatabaseUI databaseUI = DatabaseVendorUIManager.getInstance(project).get(databaseVendorInformation);
        if (databaseUI == null) {
            throw new IllegalStateException("Unsupported file");
        }
        return new NoSqlDatabaseDataEditor(databaseUI.createResultPanel(project, (NoSqlDatabaseObjectFile) file));
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "NoSqlData";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return HIDE_DEFAULT_EDITOR;
    }
}
