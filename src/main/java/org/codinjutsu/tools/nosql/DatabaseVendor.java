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

import org.codinjutsu.tools.nosql.couchbase.CouchbaseVendorInformation;
import org.codinjutsu.tools.nosql.couchbase.view.editor.CouchbaseObjectFile;
import org.codinjutsu.tools.nosql.elasticsearch.ElasticsearchVendorInformation;
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchObjectFile;
import org.codinjutsu.tools.nosql.mongo.MongoVendorInformation;
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoObjectFile;
import org.codinjutsu.tools.nosql.redis.RedisVendorInformation;
import org.codinjutsu.tools.nosql.redis.view.editor.RedisObjectFile;
import org.codinjutsu.tools.nosql.solr.SolrVendorInformation;
import org.codinjutsu.tools.nosql.solr.view.editor.SolrObjectFile;

import javax.swing.Icon;
import java.util.Arrays;

import static java.util.Arrays.stream;
import static org.codinjutsu.tools.nosql.i18n.ResourcesLoaderKt.getResourceString;

@SuppressWarnings("HardCodedStringLiteral")
public enum DatabaseVendor {

    MONGO("MongoDB", MongoObjectFile.Companion.getIcon(), "localhost:27017", getResourceString("vendor.mongo.format")) {
        @Override
        public DatabaseVendorInformation getDatabaseVendorInformation() {
            return MongoVendorInformation.INSTANCE;
        }
    },
    REDIS("RedisDB", RedisObjectFile.Companion.getIcon(), "localhost:6379", getResourceString("vendor.redis.format")) {
        @Override
        public DatabaseVendorInformation getDatabaseVendorInformation() {
            return RedisVendorInformation.INSTANCE;
        }
    },
    COUCHBASE("Couchbase", CouchbaseObjectFile.Companion.getIcon(), "localhost", getResourceString("vendor.couchbase.format")) {
        @Override
        public DatabaseVendorInformation getDatabaseVendorInformation() {
            return CouchbaseVendorInformation.INSTANCE;
        }
    },
    ELASTICSEARCH("Elasticsearch", ElasticsearchObjectFile.Companion.getIcon(), "http://localhost:9200", getResourceString("vendor.elasticsearch.format")) {
        @Override
        public DatabaseVendorInformation getDatabaseVendorInformation() {
            return ElasticsearchVendorInformation.INSTANCE;
        }
    },
    SOLR("Solr", SolrObjectFile.Companion.getIcon(), "http://localhost:8983", getResourceString("vendor.solr.format")) {
        @Override
        public DatabaseVendorInformation getDatabaseVendorInformation() {
            return SolrVendorInformation.INSTANCE;
        }
    };

    private final String name;
    private final Icon icon;
    private final String defaultUrl;
    private final String tips;

    DatabaseVendor(String name, Icon icon, String defaultUrl, String tips) {
        this.name = name;
        this.icon = icon;
        this.defaultUrl = defaultUrl;
        this.tips = tips;
    }

    public abstract DatabaseVendorInformation getDatabaseVendorInformation();

    public String getVendorName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public String getTips() {
        return tips;
    }

    @Override
    public String toString() {
        return "DatabaseVendor{name='" + name + "'}";
    }

    public static DatabaseVendor findByName(String name) {
        return stream(values())
                .filter(vendor -> vendor.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No vendor with name " + name + " found."));
    }
}
