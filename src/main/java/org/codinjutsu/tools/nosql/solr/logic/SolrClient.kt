package org.codinjutsu.tools.nosql.solr.logic

import com.google.gson.JsonObject
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.ConfigurationException
import org.codinjutsu.tools.nosql.commons.logic.LoadableDatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.JsonObjectObjectWrapper
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.solr.configuration.SolrServerConfiguration
import org.codinjutsu.tools.nosql.solr.logic.commands.GetDocument
import org.codinjutsu.tools.nosql.solr.logic.commands.ImportData
import org.codinjutsu.tools.nosql.solr.logic.commands.LoadCores
import org.codinjutsu.tools.nosql.solr.logic.commands.Search
import org.codinjutsu.tools.nosql.solr.model.SolrDatabase
import org.codinjutsu.tools.nosql.solr.view.SolrContext
import java.io.File
import java.net.URL

internal class SolrClient : LoadableDatabaseClient<SolrContext, JsonSearchResult, JsonObject> {

    override fun connect(serverConfiguration: ServerConfiguration) {
        try {
            URL(serverConfiguration.serverUrl).openConnection().connect()
        } catch (e: Exception) {
            throw ConfigurationException(e)
        }
    }

    override fun loadServer(databaseServer: DatabaseServer) {
        databaseServer.databases = LoadCores(databaseServer)
                .execute()
                .getAsJsonObject("status")
                .keySet()
                .map { SolrDatabase(it) }
    }

    override fun cleanUpServers() {
    }

    override fun registerServer(databaseServer: DatabaseServer?) {
    }

    override fun defaultConfiguration() = SolrServerConfiguration()

    override fun findDocument(context: SolrContext, _id: Any): JsonObject? {
        return GetDocument(context, _id.toString()).execute()
    }

    override fun update(context: SolrContext, document: JsonObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadRecords(context: SolrContext, query: QueryOptions): JsonSearchResult {
        val jsonObject = Search(context, query).execute()
        val response = jsonObject.getAsJsonObject("response")
        val count = response.getAsJsonPrimitive("numFound").asInt
        val objects = response.getAsJsonArray("docs").map { JsonObjectObjectWrapper(it.asJsonObject) }
        return JsonSearchResult(context.solrDatabase.name, objects, count)
    }

    override fun delete(context: SolrContext, _id: Any) {
//        createSolrClient(context.serverConfiguration).deleteById(_id.toString())
    }

    override fun importFile(context: SolrContext, file: File): ImportResultState {
        val jsonObject = ImportData(context, file).execute()
        return ImportResultState(false, "")
    }

    companion object {
        fun instance(project: Project): SolrClient {
            return ServiceManager.getService(project, SolrClient::class.java)
        }
    }
}
