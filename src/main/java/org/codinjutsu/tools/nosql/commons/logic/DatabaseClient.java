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

package org.codinjutsu.tools.nosql.commons.logic;

import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface DatabaseClient<CONTEXT, DOCUMENT, SERVERCONFIGURATION extends ServerConfiguration> {

    void connect(SERVERCONFIGURATION serverConfiguration);

    void loadServer(DatabaseServer<SERVERCONFIGURATION> databaseServer);

    void cleanUpServers();

    void registerServer(DatabaseServer<SERVERCONFIGURATION> databaseServer);

    SERVERCONFIGURATION defaultConfiguration();

    @Nullable
    DOCUMENT findDocument(CONTEXT context, @NotNull Object _id);

    void update(@NotNull CONTEXT context, @NotNull DOCUMENT document);

    void delete(@NotNull CONTEXT context, @NotNull Object _id);

    default boolean isDatabaseWithCollections() {
        return false;
    }

    @Nullable
    default Object createFolder(SERVERCONFIGURATION serverconfiguration, String parentFolderName, String folderName) {
        return null;
    }

    default void dropFolder(SERVERCONFIGURATION serverconfiguration, Object folder) {
    }

    default void dropDatabase(SERVERCONFIGURATION serverconfiguration, Database database) {
    }

    default ImportResultState importFile(CONTEXT context, File file) {
        return null;
    }
}
