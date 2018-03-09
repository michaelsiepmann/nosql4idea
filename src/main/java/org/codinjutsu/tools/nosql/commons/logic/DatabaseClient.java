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

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.model.scheme.SchemeItem;
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public interface DatabaseClient<DOCUMENT> {

    void connect(ServerConfiguration serverConfiguration);

    void loadServer(DatabaseServer databaseServer);

    void cleanUpServers();

    void registerServer(DatabaseServer databaseServer);

    @NotNull
    ServerConfiguration defaultConfiguration();

    @NotNull
    SearchResult findAll(DatabaseContext context);

    @Nullable
    DOCUMENT findDocument(DatabaseContext context, @NotNull Object _id);

    SearchResult loadRecords(DatabaseContext context, QueryOptions query);

    void update(DatabaseContext context, DOCUMENT document);

    void delete(@NotNull DatabaseContext context, @NotNull Object _id);

    default boolean isDatabaseWithCollections() {
        return false;
    }

    @Nullable
    default Object createFolder(ServerConfiguration serverconfiguration, String parentFolderName, String folderName) {
        return null;
    }

    default void dropFolder(ServerConfiguration serverconfiguration, Object folder) {
    }

    default void dropDatabase(ServerConfiguration serverconfiguration, Database database) {
    }

    default ImportResultState importFile(DatabaseContext context, File file) {
        return null;
    }

    @NotNull
    Collection<DatabaseServer> getServers();

    default Collection<DatabaseServer> filterAvailableServers() {
        return getServers().stream()
                .filter(DatabaseServer::hasDatabases)
                .collect(Collectors.toList());
    }

    @NotNull
    default SchemeItem getScheme(@NotNull DatabaseContext context) {
        return SchemeItem.Companion.getEMPTY_SCHEME();
    }
}
