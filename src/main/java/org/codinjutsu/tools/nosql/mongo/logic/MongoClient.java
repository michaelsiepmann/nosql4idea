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

package org.codinjutsu.tools.nosql.mongo.logic;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoIterable;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.exceptions.ConfigurationException;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.model.internal.DatabaseObjectObjectWrapper;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElementSearchResult;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper;
import org.codinjutsu.tools.nosql.mongo.configuration.MongoServerConfiguration;
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection;
import org.codinjutsu.tools.nosql.mongo.model.MongoContext;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.codinjutsu.tools.nosql.mongo.model.MongoQueryOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.codinjutsu.tools.nosql.mongo.model.internal.MongoHelperKt.revert;
import static org.codinjutsu.tools.nosql.mongo.model.internal.MongoHelperKt.toDatabaseElement;
import static org.codinjutsu.tools.nosql.mongo.model.internal.MongoHelperKt.toDatabaseObject;

public class MongoClient implements DatabaseClient<DatabaseElement> {

    private static final Logger LOG = Logger.getLogger(MongoClient.class);
    public static final String ID_DESCRIPTOR_KEY = "_id"; //NON-NLS
    private final List<DatabaseServer> databaseServers = new LinkedList<>();

    @NotNull
    public static MongoClient getInstance(Project project) {
        return ServiceManager.getService(project, MongoClient.class);
    }

    public void connect(ServerConfiguration configuration) {
        try (com.mongodb.MongoClient mongo = createMongoClient(configuration)) {
            getCollectionNames(mongo, configuration.getUserDatabase()).first();
        } catch (MongoException ex) {
            LOG.error("Error when accessing Mongo server", ex); //NON-NLS
            throw new MongoConnectionException(ex.getMessage());
        }
    }

    @NotNull
    private MongoIterable<String> getCollectionNames(com.mongodb.MongoClient mongo, String userDatabase) {
        if (isNotEmpty(userDatabase)) {
            return mongo.getDatabase(userDatabase).listCollectionNames();
        }
        return mongo.getDatabase("test").listCollectionNames(); //NON-NLS
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
    public MongoServerConfiguration defaultConfiguration() {
        return new MongoServerConfiguration();
    }

    public void loadServer(DatabaseServer databaseServer) {
        databaseServer.setStatus(DatabaseServer.Status.LOADING);
        List<Database> mongoDatabases = loadDatabaseCollections(databaseServer.getConfiguration());
        databaseServer.setDatabases(mongoDatabases);
        databaseServer.setStatus(DatabaseServer.Status.OK);
    }

    @NotNull
    private List<Database> loadDatabaseCollections(ServerConfiguration configuration) {
        try (com.mongodb.MongoClient mongo = createMongoClient(configuration)) {
            String userDatabase = configuration.getUserDatabase();
            if (isNotEmpty(userDatabase)) {
                List<Database> mongoDatabases = new LinkedList<>();
                mongoDatabases.add(createMongoDatabaseAndItsCollections(mongo.getDatabase(userDatabase)));
                return mongoDatabases;
            }
            List<String> databaseNames = toList(mongo.listDatabaseNames());
            Collections.sort(databaseNames);
            return databaseNames.stream()
                    .map(databaseName -> createMongoDatabaseAndItsCollections(mongo.getDatabase(databaseName)))
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (MongoException mongoEx) {
            throw new ConfigurationException(mongoEx);
        }
    }

    @NotNull
    private Database createMongoDatabaseAndItsCollections(com.mongodb.client.MongoDatabase database) {
        return new MongoDatabase(database.getName(), new HashSet<>(toList(database.listCollectionNames())));
    }

    @Override
    public void update(DatabaseContext context, DatabaseElement document) {
        withMongoClient(context, mongo -> {
            getCollection((MongoContext) context, mongo).save(revert(document));
        });
    }

    @Override
    public void delete(@NotNull DatabaseContext context, @NotNull Object _id) {
        withMongoClient(context, mongo -> {
            getCollection((MongoContext) context, mongo).remove(new BasicDBObject(ID_DESCRIPTOR_KEY, _id));
        });
    }

    private DBCollection getCollection(MongoContext context, com.mongodb.MongoClient mongo) {
        return getCollection(mongo, context.getMongoCollection());
    }

    private DBCollection getCollection(com.mongodb.MongoClient mongo, MongoCollection mongoCollection) {
        return mongo.getDB(mongoCollection.getDatabaseName()).getCollection(mongoCollection.getName());
    }

    @Override
    public void dropFolder(ServerConfiguration configuration, Object mongoCollection) {
        withMongoClient(configuration, mongo -> getCollection(mongo, (MongoCollection) mongoCollection).drop());
    }

    @Override
    public void dropDatabase(ServerConfiguration configuration, Database database) {
        withMongoClient(configuration, mongo -> mongo.dropDatabase(database.getName()));
    }

    @NotNull
    private SearchResult loadCollectionValues(MongoContext context, MongoQueryOptions mongoQueryOptions) {
        return withMongoClient(context, mongo -> {
            MongoCollection mongoCollection = context.getMongoCollection();
            DBCollection collection = getCollection(mongo, mongoCollection);
            List<ObjectWrapper> objectWrappers = mongoQueryOptions.isAggregate() ? aggregate(mongoQueryOptions, collection) : find(mongoQueryOptions, collection);
            return new DatabaseElementSearchResult(mongoCollection.getName(), objectWrappers, objectWrappers.size());
        });
    }

    @NotNull
    @Override
    public SearchResult findAll(DatabaseContext context) {
        return findOnMongoServer(context);
    }

    @NotNull
    private SearchResult findOnMongoServer(DatabaseContext context) {
        return withMongoClient(context, mongo -> {
            MongoContext mongoContext = (MongoContext) context;
            List<ObjectWrapper> list = new ArrayList<>();
            getCollection(mongoContext, mongo)
                    .find()
                    .toArray()
                    .forEach(item -> list.add(new DatabaseObjectObjectWrapper(toDatabaseObject(item))));
            return new DatabaseElementSearchResult(mongoContext.getMongoCollection().getName(), list, list.size());
        });
    }

    @Override
    @Nullable
    public DatabaseElement findDocument(DatabaseContext context, @NotNull Object _id) {
        return toDatabaseElement(findOneOnMongoServer(context, updateId(_id)));
    }

    private DBObject findOneOnMongoServer(DatabaseContext context, Object _id) {
        return withMongoClient(context, mongo -> {
            DBCollection collection = getCollection((MongoContext) context, mongo);
            return collection.findOne(new BasicDBObject(ID_DESCRIPTOR_KEY, _id));
        });
    }

    private Object updateId(Object id) {
        if (id instanceof DatabasePrimitive) {
            Object result = ((DatabasePrimitive) id).value();
            if (result != null) {
                return result;
            }
        }
        return id;
    }

    @Override
    @NotNull
    public SearchResult loadRecords(DatabaseContext context, QueryOptions query) {
        return loadCollectionValues((MongoContext) context, new MongoQueryOptions(query));
    }

    @NotNull
    private List<ObjectWrapper> aggregate(MongoQueryOptions mongoQueryOptions, DBCollection collection) {
        List<ObjectWrapper> result = new ArrayList<>();
        AggregationOutput aggregate = collection.aggregate(mongoQueryOptions.getOperations());
        int index = 0;
        Iterator<DBObject> iterator = aggregate.results().iterator();
        while (iterator.hasNext() && index++ < mongoQueryOptions.getResultLimit()) {
            result.add(new DatabaseObjectObjectWrapper(toDatabaseObject(iterator.next())));
        }
        return result;
    }

    @NotNull
    private List<ObjectWrapper> find(MongoQueryOptions mongoQueryOptions, DBCollection collection) {
        List<ObjectWrapper> result = new ArrayList<>();
        try (DBCursor cursor = createCursor(mongoQueryOptions, collection)) {
            int index = 0;
            while (cursor.hasNext() && index < mongoQueryOptions.getResultLimit()) {
                result.add(new DatabaseObjectObjectWrapper(toDatabaseObject(cursor.next())));
                index++;
            }
        }
        return result;
    }

    @NotNull
    private DBCursor createCursor(MongoQueryOptions mongoQueryOptions, DBCollection collection) {
        DBCursor cursor = findCursor(mongoQueryOptions, collection);
        DBObject sort = mongoQueryOptions.getSort();
        return sort != null ? cursor.sort(sort) : cursor;
    }

    @NotNull
    private DBCursor findCursor(MongoQueryOptions mongoQueryOptions, DBCollection collection) {
        DBObject filter = mongoQueryOptions.getFilter();
        DBObject projection = mongoQueryOptions.getProjection();
        return projection == null ? collection.find(filter) : collection.find(filter, projection);
    }

    @NotNull
    protected com.mongodb.MongoClient createMongoClient(ServerConfiguration configuration) {
        String serverUrl = configuration.getServerUrl();
        if (StringUtils.isEmpty(serverUrl)) {
            throw new ConfigurationException("server host is not set");
        }

        MongoClientURIBuilder uriBuilder = MongoClientURIBuilder.builder();
        uriBuilder.setServerAddresses(serverUrl);
        AuthenticationSettings authenticationSettings = configuration.getAuthenticationSettings();
        MongoExtraSettings mongoExtraSettings = new MongoExtraSettings(authenticationSettings.getExtras());
        if (isNotEmpty(authenticationSettings.getUsername())) {
            uriBuilder.setCredential(authenticationSettings.getUsername(), authenticationSettings.getPassword(), mongoExtraSettings.getAuthenticationDatabase());
        }


        if (mongoExtraSettings.getAuthenticationMechanism() != null) {
            uriBuilder.setAuthenticationMecanism(mongoExtraSettings.getAuthenticationMechanism());
        }

        if (mongoExtraSettings.isSsl()) {
            uriBuilder.sslEnabled();
        }

        return new com.mongodb.MongoClient(new MongoClientURI(uriBuilder.build()));
    }

    @Override
    public boolean isDatabaseWithCollections() {
        return true;
    }

    @Override
    @NotNull
    public MongoCollection createFolder(ServerConfiguration serverConfiguration, String parentFolderName, String folderName) {
        com.mongodb.client.MongoDatabase database = createMongoClient(serverConfiguration).getDatabase(parentFolderName);
        database.createCollection(folderName);
        return new MongoCollection(folderName, parentFolderName);
    }

    @NotNull
    private List<String> toList(MongoIterable<String> mongoIterable) {
        List<String> result = new ArrayList<>();
        mongoIterable.forEach((Consumer<String>) result::add);
        return result;
    }

    private void withMongoClient(DatabaseContext context, Consumer<com.mongodb.MongoClient> consumer) {
        withMongoClient(context.getServerConfiguration(), consumer);
    }

    private void withMongoClient(ServerConfiguration serverConfiguration, Consumer<com.mongodb.MongoClient> consumer) {
        try (com.mongodb.MongoClient mongo = createMongoClient(serverConfiguration)) {
            consumer.accept(mongo);
        }
    }

    private <T> T withMongoClient(DatabaseContext context, Function<com.mongodb.MongoClient, T> function) {
        try (com.mongodb.MongoClient mongo = createMongoClient(context.getServerConfiguration())) {
            return function.apply(mongo);
        }
    }
}
