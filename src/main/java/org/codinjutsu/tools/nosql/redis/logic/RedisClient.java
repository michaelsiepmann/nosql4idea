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

package org.codinjutsu.tools.nosql.redis.logic;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.SingleDatabaseObject;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration;
import org.codinjutsu.tools.nosql.redis.model.RedisContext;
import org.codinjutsu.tools.nosql.redis.model.RedisKeyType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class RedisClient implements DatabaseClient {

    private final List<DatabaseServer> databaseServers = new LinkedList<>();

    public static RedisClient getInstance(Project project) {
        return ServiceManager.getService(project, RedisClient.class);
    }

    @Override
    public void connect(ServerConfiguration serverConfiguration) {
        Jedis jedis = createJedis(serverConfiguration);
        jedis.connect();
        String userDatabase = serverConfiguration.getUserDatabase();
        int index = 0;
        if (isNotEmpty(userDatabase)) {
            index = Integer.parseInt(userDatabase);
        }
        jedis.select(index);
    }

    @Override
    public void loadServer(DatabaseServer databaseServer) {
        Jedis jedis = createJedis(databaseServer.getConfiguration());
        List<String> databaseNumberTuple = jedis.configGet("databases"); //NON-NLS
        List<Database> databases = new LinkedList<>();
        String userDatabase = databaseServer.getConfiguration().getUserDatabase();
        if (isNotEmpty(userDatabase)) {
            databases.add(new Database(userDatabase));
        } else {
            int totalNumberOfDatabase = Integer.parseInt(databaseNumberTuple.get(1));
            for (int databaseNumber = 0; databaseNumber < totalNumberOfDatabase; databaseNumber++) {
                databases.add(new Database(String.valueOf(databaseNumber)));
            }
        }
        databaseServer.setDatabases(databases);
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
        return new RedisServerConfiguration();
    }

    @Override
    public SearchResult loadRecords(DatabaseContext context, QueryOptions query) {
        Jedis jedis = createJedis(context.getServerConfiguration());
        jedis.connect();
        String name = ((RedisContext) context).getDatabase().getName();
        int index = Integer.parseInt(name);
        jedis.select(index);

        List<DatabaseObject> elements = new ArrayList<>();
        try {
            Set<String> keys = jedis.keys(query.getFilter());
            for (String key : keys) {
                RedisKeyType keyType = RedisKeyType.getKeyType(jedis.type(key));
                if (keyType != null) {
                    DatabaseElement databaseElement = keyType.toDatabaseElement(jedis, key);
                    elements.add(new SingleDatabaseObject(singletonList(key), singletonMap(key, databaseElement)));
                }
            }
        } catch (Exception ignored) {
        }
        return new SearchResult(name, elements, elements.size());
    }

    protected Jedis createJedis(ServerConfiguration serverConfiguration) {
        return new Jedis(createUri(serverConfiguration.getAuthenticationSettings().getPassword(), serverConfiguration.getServerUrl()));
    }

    @NotNull
    private String createUri(String password, String serverUrl) {
        return "redis://" + (isNotEmpty(password) ? ":" + password + "@" : "") + serverUrl; //NON-NLS
    }

    @NotNull
    @Override
    public SearchResult findAll(DatabaseContext context) {
        String name = ((RedisContext) context).getDatabase().getName();
        return new SearchResult(name, Collections.emptyList(), 0); // todo
    }

    @Nullable
    @Override
    public DatabaseElement findDocument(DatabaseContext redisPanelContext, @NotNull Object _id) {
        return null;
    }

    @Override
    public void update(@NotNull DatabaseContext redisPanelContext, @NotNull DatabaseElement databaseElement) {

    }

    @Override
    public void delete(@NotNull DatabaseContext redisPanelContext, @NotNull Object _id) {

    }
}
