package org.codinjutsu.tools.nosql.solr.view

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.commons.view.JsonResultPanel
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.solr.logic.SolrClient

internal class SolrPanel(project: Project, context: SolrContext) : DatabasePanel<SolrClient, SolrContext, JsonSearchResult, JsonObject>(project, context) {
    override fun createResultPanel(project: Project, context: SolrContext?) =
            JsonResultPanel(project, NoSQLResultPanelDocumentOperationsImpl<SolrClient, SolrContext, JsonSearchResult, JsonObject>(this))

    override fun getSearchResult(context: SolrContext, queryOptions: QueryOptions) =
            context.client.loadRecords(context, queryOptions)
}