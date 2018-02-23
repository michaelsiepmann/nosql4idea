package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.JsonResultPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.scripting.JavascriptExecutor;
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext;
import org.codinjutsu.tools.nosql.elasticsearch.scripting.ElasticsearchScriptingDatabaseWrapper;
import org.codinjutsu.tools.nosql.elasticsearch.view.panel.query.ElasticsearchQueryPanel;
import org.jetbrains.annotations.NotNull;

public class ElasticsearchPanel extends DatabasePanel<ElasticsearchClient, ElasticsearchContext, JsonSearchResult, JsonObject> implements Pageable {

    public ElasticsearchPanel(Project project, ElasticsearchContext context) {
        super(project, context, ElasticsearchQueryPanel::new);
    }

    @Override
    protected AbstractNoSQLResultPanel<JsonSearchResult, JsonObject> createResultPanel(Project project) {
        return new JsonResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this), "_id"); //NON-NLS
    }

    @Override
    protected JsonSearchResult getSearchResult(ElasticsearchContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }

    @Override
    public Object getRecords() {
        return getContext().getType();
    }

    @NotNull
    @Override
    protected JavascriptExecutor<ElasticsearchContext, DatabaseClient<ElasticsearchContext, JsonSearchResult, JsonObject>> createJavascriptExecutor(String content, Project project, ElasticsearchContext context) {
        return new JavascriptExecutor<>(content, project, new ElasticsearchScriptingDatabaseWrapper(context), context, context.getClient());
    }
}
