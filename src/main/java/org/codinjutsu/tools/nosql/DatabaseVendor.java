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
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.console.AbstractNoSQLConsoleRunner;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.codinjutsu.tools.nosql.mongo.view.console.MongoConsoleRunner;
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase;
import org.codinjutsu.tools.nosql.redis.view.console.RedisConsoleRunner;

import javax.swing.*;

public enum DatabaseVendor {

    MONGO("MongoDB", "mongodb.png", "localhost:27017", "format: host:port. If replicat set: host:port1,host:port2,...", true) {
        @Override
        public AbstractNoSQLConsoleRunner createConsoleRunner(Project project, ServerConfiguration configuration, Database database) {
            return new MongoConsoleRunner(project, configuration, (MongoDatabase) database);
        }
    },
    REDIS("RedisDB", "redis.png", "localhost:6379", "format: host:port. If cluster: host:port1,host:port2,...", true) {
        @Override
        public AbstractNoSQLConsoleRunner createConsoleRunner(Project project, ServerConfiguration configuration, Database database) {
            return new RedisConsoleRunner(project, configuration, (RedisDatabase) database);
        }
    },
    COUCHBASE("Couchbase", "couchbase.png", "localhost", "format: host:port. If cluster: host:port1,host:port2,...", false),
    ELASTICSEARCH("Elasticsearch", "", "localhost:9200", "format: http://host:port.", false);

    public final String name;
    public final Icon icon;
    public final String defaultUrl;
    public final String tips;
    public final boolean hasConsoleWindow;

    DatabaseVendor(String name, String iconFilename, String defaultUrl, String tips, boolean hasConsoleWindow) {
        this.name = name;
        this.icon = GuiUtils.loadIcon(iconFilename);
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
}
