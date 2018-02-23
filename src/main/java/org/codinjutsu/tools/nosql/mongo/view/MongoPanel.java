package org.codinjutsu.tools.nosql.mongo.view;

import com.intellij.openapi.project.Project;
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.scripting.JavascriptExecutor;
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient;
import org.codinjutsu.tools.nosql.mongo.model.MongoContext;
import org.codinjutsu.tools.nosql.mongo.model.MongoQueryOptions;
import org.codinjutsu.tools.nosql.mongo.model.MongoSearchResult;
import org.codinjutsu.tools.nosql.mongo.scripting.MongoScriptingDatabaseWrapper;
import org.codinjutsu.tools.nosql.mongo.view.panel.query.MongoQueryPanel;
import org.jetbrains.annotations.NotNull;

public class MongoPanel extends DatabasePanel<MongoClient, MongoContext, MongoSearchResult, DBObject> {

    public MongoPanel(Project project, MongoContext context) {
        super(project, context, MongoQueryPanel::new);
    }

    @Override
    protected AbstractNoSQLResultPanel<MongoSearchResult, DBObject> createResultPanel(Project project) {
        return new MongoResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this));
    }

    @Override
    public Object getRecords() {
        return getContext().getMongoCollection();
    }

    @Override
    protected MongoSearchResult getSearchResult(MongoContext context, QueryOptions queryOptions) {
        return context.getClient().loadCollectionValues(context, new MongoQueryOptions(queryOptions));
    }

    @NotNull
    @Override
    protected JavascriptExecutor<MongoContext, DatabaseClient<MongoContext, MongoSearchResult, DBObject>> createJavascriptExecutor(String content, Project project, MongoContext context) {
        return new JavascriptExecutor<>(content, project, new MongoScriptingDatabaseWrapper(context), context, context.getClient());
    }
}
