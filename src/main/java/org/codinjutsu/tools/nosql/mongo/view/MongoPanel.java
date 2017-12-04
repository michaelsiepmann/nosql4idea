package org.codinjutsu.tools.nosql.mongo.view;

import com.intellij.openapi.project.Project;
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient;
import org.codinjutsu.tools.nosql.mongo.model.MongoQueryOptions;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;
import org.codinjutsu.tools.nosql.mongo.view.panel.query.MongoQueryPanel;

public class MongoPanel extends DatabasePanel<ServerConfiguration, MongoClient, MongoContext, MongoResult, DBObject> {

    public MongoPanel(Project project, MongoContext context) {
        super(project, context, MongoQueryPanel::new);
    }

    @Override
    protected AbstractNoSQLResultPanel<MongoResult, DBObject> createResultPanel(Project project, MongoContext context) {
        return new MongoResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this));
    }

    @Override
    public Object getRecords() {
        return getContext().getMongoCollection();
    }

    @Override
    protected MongoResult getSearchResult(MongoContext context, QueryOptions queryOptions) {
        return context.getClient().loadCollectionValues(context, new MongoQueryOptions(queryOptions));
    }
}
