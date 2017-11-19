package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult;

public class ElasticsearchPanel extends DatabasePanel<ElasticsearchClient, ElasticsearchContext, ElasticsearchResult, JsonObject> {

    public ElasticsearchPanel(Project project, ElasticsearchContext elasticsearchPanelContext) {
        super(project, elasticsearchPanelContext);
    }

    @Override
    protected AbstractNoSQLResultPanel<ElasticsearchResult, JsonObject> createResultPanel(Project project, ElasticsearchContext context) {
        return new ElasticsearchResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this));
    }

    @Override
    protected ElasticsearchResult getSearchResult(ElasticsearchContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }

    @Override
    public Object getRecords() {
        return getContext().getCollection();
    }
}
