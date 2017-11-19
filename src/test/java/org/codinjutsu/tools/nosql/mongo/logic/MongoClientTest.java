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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl;
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection;
import org.codinjutsu.tools.nosql.mongo.model.MongoQueryOptions;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;
import org.codinjutsu.tools.nosql.mongo.view.MongoContext;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class MongoClientTest {

    private MongoClient mongoClient;
    private ServerConfiguration serverConfiguration;
    private QueryOptions queryOptions = new QueryOptionsImpl();
    private MongoContext context;

    @Test
    public void loadCollectionsWithEmptyFilter() {
        MongoQueryOptions mongoQueryOptions = new MongoQueryOptions(queryOptions);
        mongoQueryOptions.setResultLimit(3);
        MongoResult mongoResult = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        assertNotNull(mongoResult);
        assertEquals(3, mongoResult.getMongoObjects().size());
    }

    @Test
    public void loadCollectionsWithFilterAndProjection() {
        MongoQueryOptions mongoQueryOptions = new MongoQueryOptions(queryOptions);
        mongoQueryOptions.setFilter("{\"label\":\"tata\"}");
        mongoQueryOptions.setProjection("{\"label\":1, \"_id\": 0}");
        mongoQueryOptions.setResultLimit(3);
        MongoResult mongoResult = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        assertNotNull(mongoResult);
        assertEquals(2, mongoResult.getMongoObjects().size());
        assertEquals("[{ \"label\" : \"tata\"}, { \"label\" : \"tata\"}]", mongoResult.getMongoObjects().toString());
    }

    @Test
    public void loadCollectionsWithFilterAndProjectionAndSortByPrice() {
        MongoQueryOptions mongoQueryOptions = new MongoQueryOptions(queryOptions);
        mongoQueryOptions.setFilter("{\"label\":\"tata\"}");
        mongoQueryOptions.setProjection("{\"label\": 1, \"_id\": 0, \"price\": 1}");
        mongoQueryOptions.setSort("{\"price\": 1}");
        mongoQueryOptions.setResultLimit(3);
        MongoResult mongoResult = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        assertNotNull(mongoResult);
        assertEquals(2, mongoResult.getMongoObjects().size());
        assertEquals("[{ \"label\" : \"tata\" , \"price\" : 10}, { \"label\" : \"tata\" , \"price\" : 15}]", mongoResult.getMongoObjects().toString());
    }

    @Test
    public void updateMongoDocument() {
        MongoQueryOptions mongoQueryOptions = new MongoQueryOptions(queryOptions);
        mongoQueryOptions.setFilter("{'label': 'tete'}");
        MongoCollection mongoCollection = new MongoCollection("dummyCollection", "test");
        MongoResult initialData = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        assertEquals(1, initialData.getMongoObjects().size());
        DBObject initialMongoDocument = initialData.getMongoObjects().get(0);

        initialMongoDocument.put("price", 25);
        mongoClient.update(context, initialMongoDocument);

        MongoResult updatedResult = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        List<DBObject> updatedMongoDocuments = updatedResult.getMongoObjects();
        assertEquals(1, updatedMongoDocuments.size());
        DBObject updatedMongoDocument = updatedMongoDocuments.get(0);

        assertEquals(25, updatedMongoDocument.get("price"));
    }


    @Test
    public void deleteMongoDocument() {
        MongoQueryOptions mongoQueryOptions = new MongoQueryOptions(queryOptions);
        mongoQueryOptions.setFilter("{'label': 'tete'}");
        MongoCollection mongoCollection = new MongoCollection("dummyCollection", "test");
        MongoResult initialData = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        assertEquals(1, initialData.getMongoObjects().size());
        DBObject initialMongoDocument = initialData.getMongoObjects().get(0);

        mongoClient.delete(context, initialMongoDocument.get("_id"));

        MongoResult deleteResult = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        List<DBObject> updatedMongoDocuments = deleteResult.getMongoObjects();
        assertEquals(0, updatedMongoDocuments.size());
    }


    @Test
    public void loadCollectionsWithAggregateOperators() {
        MongoQueryOptions mongoQueryOptions = new MongoQueryOptions(queryOptions);
        mongoQueryOptions.setOperations("[{'$match': {'price': 15}}, {'$project': {'label': 1, 'price': 1}}, {'$group': {'_id': '$label', 'total': {'$sum': '$price'}}}]");
        MongoResult mongoResult = mongoClient.loadCollectionValues(context, mongoQueryOptions);
        assertNotNull(mongoResult);

        List<DBObject> mongoObjects = mongoResult.getMongoObjects();

        assertEquals(2, mongoObjects.size());
        assertEquals("{ \"_id\" : \"tutu\" , \"total\" : 15}", mongoObjects.get(0).toString());
        assertEquals("{ \"_id\" : \"tata\" , \"total\" : 15}", mongoObjects.get(1).toString());
    }

    @Before
    public void setUp() throws Exception {
        com.mongodb.MongoClient mongo = new com.mongodb.MongoClient("localhost:27017");
        MongoDatabase db = mongo.getDatabase("test");

        com.mongodb.client.MongoCollection<Document> dummyCollection = db.getCollection("dummyCollection");
        dummyCollection.deleteMany(new BasicDBObject());
        fillCollectionWithJsonData(dummyCollection, IOUtils.toString(getClass().getResourceAsStream("dummyCollection.json")));

        mongoClient = new MongoClient();
        serverConfiguration = mongoClient.defaultConfiguration();
        serverConfiguration.setServerUrl("localhost:27017");
        context = new MongoContext(null, serverConfiguration, new MongoCollection("dummyCollection", "test"));
    }

    private static void fillCollectionWithJsonData(com.mongodb.client.MongoCollection<Document> collection, String jsonResource) {
        Object jsonParsed = JSON.parse(jsonResource);
        if (jsonParsed instanceof BasicDBList) {
            BasicDBList jsonObject = (BasicDBList) jsonParsed;
            for (Object o : jsonObject) {
                DBObject dbObject = (DBObject) o;
                Document document = new Document();
                for (String key : dbObject.keySet()) {
                    document.append(key, dbObject.get(key));
                }
                collection.insertOne(document);
            }
        } else {
            DBObject dbObject = (DBObject) jsonParsed;
            Document document = new Document();
            for (String key : dbObject.keySet()) {
                document.append(key, dbObject.get(key));
            }
            collection.insertOne(document);
        }
    }
}

