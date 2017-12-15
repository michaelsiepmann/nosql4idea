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

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder;
import org.codinjutsu.tools.nosql.commons.view.console.AbstractNoSQLConsoleRunner;
import org.codinjutsu.tools.nosql.couchbase.model.explorer.CouchbaseDatabaseServerFolder;
import org.codinjutsu.tools.nosql.couchbase.view.editor.CouchbaseFakeFileType;
import org.codinjutsu.tools.nosql.elasticsearch.model.explorer.ElasticsearchDatabaseServerFolder;
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchFakeFileType;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.codinjutsu.tools.nosql.mongo.model.explorer.MongoDatabaseServerFolder;
import org.codinjutsu.tools.nosql.mongo.view.console.MongoConsoleRunner;
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoFakeFileType;
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase;
import org.codinjutsu.tools.nosql.redis.model.explorer.RedisDatabaseServerFolder;
import org.codinjutsu.tools.nosql.redis.view.console.RedisConsoleRunner;
import org.codinjutsu.tools.nosql.redis.view.editor.RedisFakeFileType;

import javax.swing.*;

public enum DatabaseVendor {

    MONGO("MongoDB", MongoFakeFileType.INSTANCE.getIcon(), "localhost:27017", "format: host:port. If replicat set: host:port1,host:port2,...", true) {
        @Override
        public AbstractNoSQLConsoleRunner createConsoleRunner(Project project, ServerConfiguration configuration, Database database) {
            return new MongoConsoleRunner(project, configuration, (MongoDatabase) database);
        }

        @Override
        public DatabaseServerFolder createDatabaseServerFolder(DatabaseServer databaseServer, DatabaseVendorClientManager databaseVendorClientManager) {
            return new MongoDatabaseServerFolder(databaseServer, databaseVendorClientManager);
        }
    },
    REDIS("RedisDB", RedisFakeFileType.REDIS_ICON, "localhost:6379", "format: host:port. If cluster: host:port1,host:port2,...", true) {
        @Override
        public AbstractNoSQLConsoleRunner createConsoleRunner(Project project, ServerConfiguration configuration, Database database) {
            return new RedisConsoleRunner(project, configuration, (RedisDatabase) database);
        }

        @Override
        public DatabaseServerFolder createDatabaseServerFolder(DatabaseServer databaseServer, DatabaseVendorClientManager databaseVendorClientManager) {
            return new RedisDatabaseServerFolder(databaseServer);
        }
    },
    COUCHBASE("Couchbase", CouchbaseFakeFileType.INSTANCE.getIcon(), "localhost", "format: host:port. If cluster: host:port1,host:port2,...", false) {
        @Override
        public DatabaseServerFolder createDatabaseServerFolder(DatabaseServer databaseServer, DatabaseVendorClientManager databaseVendorClientManager) {
            return new CouchbaseDatabaseServerFolder(databaseServer);
        }
    },
    ELASTICSEARCH("Elasticsearch", ElasticsearchFakeFileType.INSTANCE.getIcon(), "http://localhost:9200", "format: http://host:port.", false) {
        @Override
        public DatabaseServerFolder createDatabaseServerFolder(DatabaseServer databaseServer, DatabaseVendorClientManager databaseVendorClientManager) {
            return new ElasticsearchDatabaseServerFolder(databaseServer, databaseVendorClientManager);
        }
    };

    public final String name;
    public final Icon icon;
    public final String defaultUrl;
    public final String tips;
    public final boolean hasConsoleWindow;

    DatabaseVendor(String name, Icon icon, String defaultUrl, String tips, boolean hasConsoleWindow) {
        this.name = name;
        this.icon = icon;
        this.defaultUrl = defaultUrl;
        this.tips = tips;
        this.hasConsoleWindow = hasConsoleWindow;
    }

    public AbstractNoSQLConsoleRunner createConsoleRunner(Project project, ServerConfiguration configuration, Database database) {
        return null;
    }

    @Override
    public String toString() {
        return "DatabaseVendor{name='" + name + "'}";
    }

    public abstract DatabaseServerFolder createDatabaseServerFolder(DatabaseServer databaseServer, DatabaseVendorClientManager databaseVendorClientManager);
}
