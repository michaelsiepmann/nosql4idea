package org.codinjutsu.tools.nosql.elasticsearch.logic

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.ConfigurationException
import org.codinjutsu.tools.nosql.commons.logic.LoadableDatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.JsonObjectObjectWrapper
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.elasticsearch.configuration.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.BulkImport
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.CreateType
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.DeleteElement
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.FetchDocument
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetIndices
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetTypes
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Insert
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Search
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion.VERSION_20
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext
import java.io.File
import java.net.URL

internal class ElasticsearchClient : LoadableDatabaseClient<ElasticsearchContext, JsonSearchResult, JsonObject> {

    override fun connect(serverConfiguration: ServerConfiguration) {
        try {
            URL(serverConfiguration.serverUrl).openConnection().connect()
        } catch (e: Exception) {
            throw ConfigurationException(e)
        }
    }

    override fun loadServer(databaseServer: DatabaseServer) {
        val databases = GetIndices(databaseServer.configuration.serverUrl)
                .execute()
                .entrySet()
                .map {
                    ElasticsearchDatabase(it.key, getTypes((databaseServer.configuration as ElasticsearchServerConfiguration), it.key))
                }
        databaseServer.databases.addAll(databases)
    }

    override fun cleanUpServers() {
    }

    override fun registerServer(databaseServer: DatabaseServer?) {
    }

    override fun defaultConfiguration() =
            ElasticsearchServerConfiguration(VERSION_20)

    override fun loadRecords(context: ElasticsearchContext, queryOptions: QueryOptions): JsonSearchResult {
        val searchResult = Search(context, queryOptions).execute()
        val hits = searchResult.getAsJsonObject("hits")
        val jsonArray = hits?.getAsJsonArray("hits") ?: JsonArray()
        val objectWrappers = jsonArray.map { JsonObjectObjectWrapper(it.asJsonObject) }
        val totalCount = hits?.get("total")?.asInt ?: 0
        return JsonSearchResult(context.database.name, objectWrappers, totalCount)
    }

    override fun dropFolder(configuration: ServerConfiguration, type: Any) {
        if (type is ElasticsearchType) {
            DeleteElement("${configuration.serverUrl}/${type.databaseName}/${type.name}").execute()
        }
    }

    override fun dropDatabase(configuration: ServerConfiguration, database: Database) {
        DeleteElement("${configuration.serverUrl}/${database.name}").execute()
    }

    override fun findDocument(context: ElasticsearchContext, id: Any): JsonObject? {
        return FetchDocument(context, id.toString()).execute()
    }

    override fun update(context: ElasticsearchContext, document: JsonObject) {
        Insert(context, document).execute()
    }

    override fun delete(context: ElasticsearchContext, _id: Any) {
        val serverConfiguration = context.serverConfiguration
        val database = context.database
        val type = context.type
        if (type != null) {
            DeleteElement("${serverConfiguration.serverUrl}/${database.name}/${type.name}/$_id").execute()
        }
    }

    private fun getTypes(configuration: ElasticsearchServerConfiguration, index: String): MutableCollection<ElasticsearchType> {
        return GetTypes(configuration.serverUrl, index)
                .execute()
                .getAsJsonObject(index)
                .getAsJsonObject("mappings")
                .entrySet()
                .map { ElasticsearchType(it.key, index, configuration.version) }
                .toMutableList()
    }

    override fun isDatabaseWithCollections() = true

    override fun createFolder(serverConfiguration: ServerConfiguration, parentFolderName: String, folderName: String): ElasticsearchType {
        CreateType(serverConfiguration.serverUrl, parentFolderName, folderName).execute()
        return ElasticsearchType(folderName, parentFolderName, (serverConfiguration as ElasticsearchServerConfiguration).version)
    }

    override fun importFile(context: ElasticsearchContext, file: File): ImportResultState {
        val result = BulkImport(context.serverConfiguration.serverUrl, file).execute()
        return ImportResultState(false, "")
    }

    companion object {
        fun getInstance(project: Project): ElasticsearchClient = ServiceManager.getService(project, ElasticsearchClient::class.java)
    }
}