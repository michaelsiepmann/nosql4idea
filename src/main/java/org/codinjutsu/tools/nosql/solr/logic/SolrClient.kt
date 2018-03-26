package org.codinjutsu.tools.nosql.solr.logic

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.exceptions.ConfigurationException
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseObjectImpl
import org.codinjutsu.tools.nosql.commons.model.internal.toDatabaseElement
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.solr.configuration.SolrServerConfiguration
import org.codinjutsu.tools.nosql.solr.logic.commands.GetDocument
import org.codinjutsu.tools.nosql.solr.logic.commands.ImportData
import org.codinjutsu.tools.nosql.solr.logic.commands.LoadCores
import org.codinjutsu.tools.nosql.solr.logic.commands.Search
import org.codinjutsu.tools.nosql.solr.model.SolrContext
import java.io.File
import java.net.URL

internal class SolrClient : DatabaseClient {

    private val databaseServers = mutableListOf<DatabaseServer>()

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
                .map { Database(it) }
    }

    override fun cleanUpServers() {
        databaseServers.clear()
    }

    override fun registerServer(databaseServer: DatabaseServer) {
        databaseServers.add(databaseServer)
    }

    override fun getServers() = databaseServers

    override fun defaultConfiguration() = SolrServerConfiguration()

    override fun findAll(context: DatabaseContext) = jsonSearchResult(DatabaseObjectImpl(), context) // todo

    override fun findDocument(context: DatabaseContext, _id: Any) =
            GetDocument(context as SolrContext, _id.toString()).execute().toDatabaseElement()

    override fun update(context: DatabaseContext, document: DatabaseElement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadRecords(context: DatabaseContext, query: QueryOptions) =
            jsonSearchResult(Search(context as SolrContext, query).execute().toDatabaseElement(), context)

    private fun jsonSearchResult(jsonObject: DatabaseObject, context: DatabaseContext): SearchResult {
        val response = jsonObject.getAsDatabaseObject("response")
        val count = response?.getAsDatabasePrimitive("numFound")?.asInt() ?: 0
        val objects = response?.getAsDatabaseArray("docs")?.map { it.asObject() } ?: emptyList()
        return SearchResult((context as SolrContext).solrDatabase.name, objects, count)
    }

    override fun delete(context: DatabaseContext, _id: Any) {
//        createSolrClient(context.serverConfiguration).deleteById(_id.toString())
    }

    override fun importFile(context: DatabaseContext, file: File): ImportResultState {
        val jsonObject = ImportData(context as SolrContext, file).execute()
        return ImportResultState(false, "")
    }

    companion object {
        fun instance(project: Project): SolrClient {
            return ServiceManager.getService(project, SolrClient::class.java)
        }
    }
}
