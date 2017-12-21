package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.JsonResultPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext;
import org.codinjutsu.tools.nosql.elasticsearch.view.panel.query.ElasticsearchQueryPanel;

public class ElasticsearchPanel extends DatabasePanel<ElasticsearchClient, ElasticsearchContext, JsonSearchResult, JsonObject> implements Pageable {

    public ElasticsearchPanel(Project project, ElasticsearchContext context) {
        super(project, context, ElasticsearchQueryPanel::new);
    }

    @Override
    protected AbstractNoSQLResultPanel<JsonSearchResult, JsonObject> createResultPanel(Project project, ElasticsearchContext context) {
        return new JsonResultPanel(project, new NoSQLResultPanelDocumentOperationsImpl<>(this), "_id");
    }

    @Override
    protected JsonSearchResult getSearchResult(ElasticsearchContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }

    @Override
    public Object getRecords() {
        return getContext().getType();
    }
}
