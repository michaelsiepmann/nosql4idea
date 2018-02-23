package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.scripting.JavascriptExecutor;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseSearchResult;
import org.codinjutsu.tools.nosql.couchbase.scripting.CouchbaseScriptingDatabaseWrapper;
import org.jetbrains.annotations.NotNull;

public class CouchbasePanel extends DatabasePanel<CouchbaseClient, CouchbaseContext, CouchbaseSearchResult, JsonObject> {

    public CouchbasePanel(Project project, CouchbaseContext context) {
        super(project, context);
    }

    @Override
    protected AbstractNoSQLResultPanel<CouchbaseSearchResult, JsonObject> createResultPanel(Project project) {
        return new CouchbaseResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this));
    }

    @Override
    protected CouchbaseSearchResult getSearchResult(CouchbaseContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }

    @NotNull
    @Override
    protected JavascriptExecutor<CouchbaseContext, DatabaseClient<CouchbaseContext, CouchbaseSearchResult, JsonObject>> createJavascriptExecutor(String content, Project project, CouchbaseContext context) {
        return new JavascriptExecutor<>(content, project, new CouchbaseScriptingDatabaseWrapper(context), context, context.getClient());
    }
}
