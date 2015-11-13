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

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.codinjutsu.tools.nosql.couchbase.view.CouchbasePanel;
import org.codinjutsu.tools.nosql.couchbase.view.editor.CouchbaseObjectFile;
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient;
import org.codinjutsu.tools.nosql.mongo.view.MongoPanel;
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoObjectFile;
import org.codinjutsu.tools.nosql.redis.logic.RedisClient;
import org.codinjutsu.tools.nosql.redis.view.RedisPanel;
import org.codinjutsu.tools.nosql.redis.view.editor.RedisObjectFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class NoSqlDatabaseDataEditorProvider implements FileEditorProvider, ApplicationComponent, DumbAware {


    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file instanceof MongoObjectFile || file instanceof RedisObjectFile || file instanceof CouchbaseObjectFile;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        if (file instanceof MongoObjectFile) {
            MongoObjectFile mongoObjectFile = (MongoObjectFile) file;
            return new NoSqlDatabaseDataEditor(new MongoPanel(project, MongoClient.getInstance(project), mongoObjectFile.getConfiguration(), mongoObjectFile.getCollection()));
        } else if (file instanceof RedisObjectFile) {
            RedisObjectFile redisObjectFile = (RedisObjectFile) file;
            return new NoSqlDatabaseDataEditor(new RedisPanel(project, RedisClient.getInstance(project), redisObjectFile.getConfiguration(), redisObjectFile.getDatabase()));
        } else if (file instanceof CouchbaseObjectFile) {
            CouchbaseObjectFile couchbaseObjectFile = (CouchbaseObjectFile) file;
            return new NoSqlDatabaseDataEditor(new CouchbasePanel(project, CouchbaseClient.getInstance(project), couchbaseObjectFile.getConfiguration(), couchbaseObjectFile.getDatabase()));
        }
        throw new IllegalStateException("Unsupported file");
    }

    @Override
    public void disposeEditor(@NotNull FileEditor editor) {
        editor.dispose();
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project, @NotNull VirtualFile file) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {

    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "NoSqlData";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "NoSqlPlugin.NoSqlEditorProvider";
    }
}
