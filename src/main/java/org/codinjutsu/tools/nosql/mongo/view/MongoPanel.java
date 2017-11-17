package org.codinjutsu.tools.nosql.mongo.view;

import com.intellij.openapi.project.Project;
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient;
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection;
import org.codinjutsu.tools.nosql.mongo.model.MongoQueryOptions;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;

public class MongoPanel extends DatabasePanel<MongoClient, MongoCollection, MongoResult, DBObject> {

    public MongoPanel(Project project, MongoClient client, ServerConfiguration configuration, MongoCollection collection) {
        super(project, client, configuration, collection);
    }

    @Override
    protected AbstractNoSQLResultPanel<MongoResult, DBObject> createResultPanel(Project project, MongoClient mongoClient, ServerConfiguration configuration, MongoCollection mongoCollection) {
        return new MongoResultPanel(project, new NoSQLResultPanelDocumentOperations<DBObject>() {

            public DBObject getDocument(Object _id) {
                return mongoClient.findMongoDocument(configuration, mongoCollection, _id);
            }

            public void updateDocument(DBObject document) {
                mongoClient.update(configuration, mongoCollection, document);
                executeQuery();
            }

            public void deleteDocument(Object document) {
                mongoClient.delete(configuration, mongoCollection, document);
                executeQuery();
            }
        });
    }

    @Override
    protected MongoResult getSearchResult(MongoClient mongoClient, ServerConfiguration configuration, MongoCollection mongoCollection, QueryOptions queryOptions) {
        return mongoClient.loadCollectionValues(configuration, mongoCollection, new MongoQueryOptions(queryOptions));
    }
}
