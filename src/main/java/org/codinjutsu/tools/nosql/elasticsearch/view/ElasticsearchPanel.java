package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchQuery;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult;
import org.jetbrains.annotations.NotNull;

public class ElasticsearchPanel extends DatabasePanel<ElasticsearchClient, ElasticsearchCollection, ElasticsearchResult, JsonObject> {

    private final ElasticsearchDatabase database;

    public ElasticsearchPanel(Project project, ElasticsearchClient client, ServerConfiguration serverConfiguration, ElasticsearchDatabase database, ElasticsearchCollection collection) {
        super(project, client, serverConfiguration, collection);
        this.database = database;
    }

    @Override
    protected AbstractNoSQLResultPanel<ElasticsearchResult, JsonObject> createResultPanel(Project project, ElasticsearchClient client, ServerConfiguration configuration, ElasticsearchCollection collection) {
        return new ElasticsearchResultPanel(project, new NoSQLResultPanelDocumentOperations<JsonObject>() {
            @Override
            public JsonObject getDocument(@NotNull Object _id) {
                return client.findDocument(configuration, database, collection, _id);
            }

            @Override
            public void deleteDocument(@NotNull Object document) {
                client.delete(configuration, database, collection, document);
                executeQuery();
            }

            @Override
            public void updateDocument(JsonObject document) {
                client.update(configuration, database, collection, document);
                executeQuery();
            }
        });
    }

    @Override
    protected ElasticsearchResult getSearchResult(ElasticsearchClient client, ServerConfiguration configuration, ElasticsearchCollection collection, QueryOptions queryOptions) {
        return client.loadRecords(configuration, database, new ElasticsearchQuery(queryOptions.getResultLimit(), collection));
    }
}
