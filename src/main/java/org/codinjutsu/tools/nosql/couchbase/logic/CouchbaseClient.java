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

package org.codinjutsu.tools.nosql.couchbase.logic;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.exceptions.ConfigurationException;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.couchbase.configuration.CouchbaseServerConfiguration;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseJsonConverterKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class CouchbaseClient implements DatabaseClient {

    private final List<DatabaseServer> databaseServers = new LinkedList<>();

    public static CouchbaseClient getInstance(Project project) {
        return ServiceManager.getService(project, CouchbaseClient.class);
    }

    @Override
    public void connect(ServerConfiguration serverConfiguration) {
        CouchbaseCluster cluster = createCluster(serverConfiguration);
        String userDatabase = serverConfiguration.getUserDatabase();
        Bucket bucket = null;
        try {
            bucket = openBucket(cluster, userDatabase);
        } catch (Exception ex) {
            throw new ConfigurationException(ex);
        } finally {
            if (bucket != null) {
                bucket.close();
            }
            cluster.disconnect();
        }
    }

    private Bucket openBucket(CouchbaseCluster cluster, String userDatabase) {
        return isEmpty(userDatabase) ? cluster.openBucket() : cluster.openBucket(userDatabase);
    }

    @NotNull
    CouchbaseCluster createCluster(ServerConfiguration serverConfiguration) {
        return CouchbaseCluster.create(serverConfiguration.getServerUrl());
    }

    @Override
    public void loadServer(DatabaseServer databaseServer) {
        Cluster cluster = createCluster(databaseServer.getConfiguration());
        databaseServer.setDatabases(collectCouchDatabases(databaseServer, cluster));
        cluster.disconnect();
    }

    @NotNull
    private List<Database> collectCouchDatabases(DatabaseServer databaseServer, Cluster cluster) {
        AuthenticationSettings authenticationSettings = databaseServer.getConfiguration().getAuthenticationSettings();
        ClusterManager clusterManager = cluster.clusterManager(authenticationSettings.getUsername(), authenticationSettings.getPassword());

        String userBucket = databaseServer.getConfiguration().getUserDatabase();
        if (isNotBlank(userBucket)) {
            return singletonList(new Database(clusterManager.getBucket(userBucket).name()));
        }

        return clusterManager.getBuckets()
                .stream()
                .map(bucketSettings -> new Database(bucketSettings.name()))
                .collect(Collectors.toList());
    }

    public void cleanUpServers() {
        databaseServers.clear();
    }

    public void registerServer(DatabaseServer databaseServer) {
        databaseServers.add(databaseServer);
    }

    @Override
    @NotNull
    public Collection<DatabaseServer> getServers() {
        return databaseServers;
    }

    @NotNull
    @Override
    public ServerConfiguration defaultConfiguration() {
        return new CouchbaseServerConfiguration();
    }

    @Override
    public SearchResult loadRecords(DatabaseContext context, QueryOptions queryOptions) {
        ServerConfiguration configuration = context.getServerConfiguration();
        Database database = ((CouchbaseContext) context).getDatabase();
        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment.builder().build(), configuration.getServerUrl());
//        AuthenticationSettings authenticationSettings = configuration.getAuthenticationSettings();
//        ClusterManager clusterManager = cluster.clusterManager(authenticationSettings.getUsername(), authenticationSettings.getPassword());

        Bucket bucket = cluster.openBucket(database.getName(), 10, TimeUnit.SECONDS);
        N1qlQueryResult queryResult = bucket.query(N1qlQuery.simple(select("*").from(i(database.getName())).limit(queryOptions.getResultLimit())));

//TODO dirty zone :(
        List<DatabaseObject> elements = new ArrayList<>();
/*
todo
        List<JsonObject> errors = queryResult.errors();
        if (!errors.isEmpty()) {
            cluster.disconnect();
            result.addErrors(errors);
            return result;
        }
*/

        for (N1qlQueryRow row : queryResult.allRows()) {
            elements.add(CouchbaseJsonConverterKt.toDatabaseElement(row.value()));
        }
        cluster.disconnect();
        return new SearchResult(database.getName(), elements, elements.size());
    }

    @NotNull
    @Override
    public SearchResult findAll(DatabaseContext context) {
/*
        todo
        ServerConfiguration configuration = context.getServerConfiguration();
        Database database = context.getDatabase();
        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment.builder().build(), configuration.getServerUrl());
        Bucket bucket = cluster.openBucket(database.getName(), 10, TimeUnit.SECONDS);
*/
        return new SearchResult(((CouchbaseContext) context).getDatabase().getName(), Collections.emptyList(), 0);
    }

    @Nullable
    @Override
    public DatabaseElement findDocument(DatabaseContext context, @NotNull Object _id) {
        return null;
    }

    @Override
    public void update(@NotNull DatabaseContext context, @NotNull DatabaseElement databaseElement) {
    }

    @Override
    public void delete(@NotNull DatabaseContext context, @NotNull Object _id) {
    }
}
