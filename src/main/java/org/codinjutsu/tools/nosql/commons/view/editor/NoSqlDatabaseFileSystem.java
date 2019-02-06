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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoSqlDatabaseFileSystem extends VirtualFileSystem {

    private static final String PROTOCOL = "nosql"; //NON-NLS

    public static NoSqlDatabaseFileSystem getInstance() {
        return ApplicationManager.getApplication().getComponent(NoSqlDatabaseFileSystem.class);
    }

    @NotNull
    @Override
    public String getProtocol() {
        return PROTOCOL;
    }

    public void openEditor(@NotNull final NoSqlDatabaseObjectFile databaseObjectFile) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(databaseObjectFile.getProject());
        fileEditorManager.openFile(databaseObjectFile, true);
    }

//    Unused methods

    @Nullable
    @Override
    public VirtualFile findFileByPath(@NotNull @NonNls String path) {
        return null;
    }

    @Override
    public void refresh(boolean asynchronous) {
    }

    @Nullable
    @Override
    public VirtualFile refreshAndFindFileByPath(@NotNull String path) {
        return null;
    }

    @Override
    public void addVirtualFileListener(@NotNull VirtualFileListener listener) {

    }

    @Override
    public void removeVirtualFileListener(@NotNull VirtualFileListener listener) {

    }

    @Override
    protected void deleteFile(Object requestor, @NotNull VirtualFile vFile) {

    }

    @Override
    protected void moveFile(Object requestor, @NotNull VirtualFile vFile, @NotNull VirtualFile newParent) {

    }

    @Override
    protected void renameFile(Object requestor, @NotNull VirtualFile vFile, @NotNull String newName) {
    }

    @Override
    protected VirtualFile createChildFile(Object requestor, @NotNull VirtualFile vDir, @NotNull String fileName) {
        return null;
    }

    @NotNull
    @Override
    protected VirtualFile createChildDirectory(Object requestor, @NotNull VirtualFile vDir, @NotNull String dirName) {
        throw new UnsupportedOperationException("No file management in this plugin");
    }

    @Override
    protected VirtualFile copyFile(Object requestor, @NotNull VirtualFile virtualFile, @NotNull VirtualFile newParent, @NotNull String copyName) {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
