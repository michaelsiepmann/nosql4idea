package org.codinjutsu.tools.nosql.solr.view

import com.intellij.openapi.project.Project
import org.apache.solr.common.SolrDocument
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.solr.logic.SolrClient
import org.codinjutsu.tools.nosql.solr.model.SolrResult

internal class SolrPanel(project: Project, context: SolrContext) : DatabasePanel<SolrClient, SolrContext, SolrResult, SolrDocument>(project, context) {
    override fun createResultPanel(project: Project, context: SolrContext?) =
            SolrResultPanel(project, NoSQLResultPanelDocumentOperationsImpl<SolrClient, SolrContext, SolrResult, SolrDocument>(this))

    override fun getSearchResult(context: SolrContext, queryOptions: QueryOptions?) =
            context.client.loadRecords(context, queryOptions)
}