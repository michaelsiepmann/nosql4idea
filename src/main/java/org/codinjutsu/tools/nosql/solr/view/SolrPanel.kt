package org.codinjutsu.tools.nosql.solr.view

import com.google.gson.JsonObject
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.commons.view.JsonResultPanel
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperationsImpl
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.commons.view.scripting.JavascriptExecutor
import org.codinjutsu.tools.nosql.solr.logic.SolrClient
import org.codinjutsu.tools.nosql.solr.model.SolrContext
import org.codinjutsu.tools.nosql.solr.scripting.SolrScriptingDatabaseWrapper

internal class SolrPanel(project: Project, context: SolrContext) : DatabasePanel<SolrClient, SolrContext, JsonSearchResult, JsonObject>(project, context) {
    override fun createResultPanel(project: Project): AbstractNoSQLResultPanel<JsonSearchResult, JsonObject>? =
            JsonResultPanel(project, NoSQLResultPanelDocumentOperationsImpl<SolrClient, SolrContext, JsonSearchResult, JsonObject>(this), "id")

    override fun getSearchResult(context: SolrContext, queryOptions: QueryOptions) =
            context.client.loadRecords(context, queryOptions)

    override fun createJavascriptExecutor(content: String, project: Project, context: SolrContext): JavascriptExecutor<SolrContext, DatabaseClient<SolrContext, JsonSearchResult, JsonObject>> =
            JavascriptExecutor(content, project, SolrScriptingDatabaseWrapper(context), context, context.client)
}