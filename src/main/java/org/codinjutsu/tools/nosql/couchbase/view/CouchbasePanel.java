package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseResult;

public class CouchbasePanel extends DatabasePanel<CouchbaseClient, CouchbaseContext, CouchbaseResult, JsonObject> {

    public CouchbasePanel(Project project, CouchbaseContext context) {
        super(project, context);
    }

    @Override
    protected AbstractNoSQLResultPanel<CouchbaseResult, JsonObject> createResultPanel(Project project, CouchbaseContext context) {
        return new CouchbaseResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this));
    }

    @Override
    protected CouchbaseResult getSearchResult(CouchbaseContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }
}
