package org.codinjutsu.tools.nosql.elasticsearch.logic

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.exceptions.ConfigurationException
import org.codinjutsu.tools.nosql.commons.exceptions.DatabaseException
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.JsonObjectObjectWrapper
import org.codinjutsu.tools.nosql.commons.model.JsonSearchResult
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.model.scheme.SchemeItem
import org.codinjutsu.tools.nosql.commons.model.scheme.SchemeItem.Companion.EMPTY_SCHEME
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.elasticsearch.configuration.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.BulkImport
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.CreateType
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.DeleteElement
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.FetchAllDocuments
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.FetchDocument
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetIndices
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetMapping
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetTypes
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Insert
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Search
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion.VERSION_20
import java.io.File
import java.net.URL

internal class ElasticsearchClient : DatabaseClient<JsonObject> {

    private val databaseServers = mutableListOf<DatabaseServer>()

    override fun connect(serverConfiguration: ServerConfiguration) {
        try {
            URL(serverConfiguration.serverUrl).openConnection().connect()
        } catch (e: Exception) {
            throw ConfigurationException(e)
        }
    }

    override fun loadServer(databaseServer: DatabaseServer) {
        try {
            val configuration = databaseServer.configuration as ElasticsearchServerConfiguration
            val databases = GetIndices(configuration.serverUrl)
                    .execute()
                    .keySet()
                    .map {
                        ElasticsearchDatabase(it, getTypes(configuration, it))
                    }
            databaseServer.databases.apply {
                clear()
                addAll(databases)
            }
        } catch (e: Exception) {
            throw DatabaseException(e)
        }
    }

    override fun cleanUpServers() {
        databaseServers.clear()
    }

    override fun registerServer(databaseServer: DatabaseServer) {
        databaseServers.add(databaseServer)
    }

    override fun getServers() = databaseServers

    override fun defaultConfiguration() =
            ElasticsearchServerConfiguration(VERSION_20)

    override fun loadRecords(context: DatabaseContext, queryOptions: QueryOptions): SearchResult {
        return jsonSearchResult(Search(context as ElasticsearchContext, queryOptions).execute(), context)
    }

    private fun jsonSearchResult(searchResult: JsonObject, context: ElasticsearchContext): JsonSearchResult {
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

    override fun findAll(context: DatabaseContext): JsonSearchResult {
        return jsonSearchResult(FetchAllDocuments(context as ElasticsearchContext).execute(), context)
    }

    override fun findDocument(context: DatabaseContext, id: Any) =
            FetchDocument(context as ElasticsearchContext, id.toString()).execute()

    override fun update(context: DatabaseContext, document: JsonObject) {
        val id = document.getAsJsonPrimitive("_id").asString
        val result = Insert(context as ElasticsearchContext, document.getAsJsonObject("_source")
                ?: document, id).execute()
    }

    override fun delete(context: DatabaseContext, _id: Any) {
        val serverConfiguration = context.serverConfiguration
        if (context is ElasticsearchContext) {
            val database = context.database
            val type = context.type
            if (type != null) {
                DeleteElement("${serverConfiguration.serverUrl}/${database.name}/${type.name}/$_id").execute()
            }
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

    override fun getScheme(context: DatabaseContext): SchemeItem {
        val serverConfiguration = context.serverConfiguration
        if (context is ElasticsearchContext) {
            val type = context.type
            if (type != null) {
                val index = context.database.name
                val indexType = type.name
                return convertToScheme(
                        GetMapping(serverConfiguration.serverUrl, index, indexType).execute(),
                        index,
                        indexType
                )
            }
        }
        return EMPTY_SCHEME
    }

    private fun convertToScheme(json: JsonObject, index: String, indexType: String): SchemeItem {
        val typeProperties = json.getAsJsonObject(index).getAsJsonObject("mappings").getAsJsonObject(indexType).getAsJsonObject("properties")
        return SchemeItem("_source", DataType.STRING, collectSchemes(typeProperties))
    }

    private fun collectSchemes(properties: JsonObject): List<SchemeItem> =
            properties.entrySet()
                    .map { (key, value) -> schemeItem(key, value.asJsonObject) }

    private fun schemeItem(key: String, value: JsonObject): SchemeItem {
        val properties = value.get("properties")
        if (properties != null) {
            return SchemeItem(key, DataType.STRING, collectSchemes(properties.asJsonObject))
        }
        return SchemeItem(key, calcDataType(value), emptyList())
    }

    private fun calcDataType(value: JsonObject) =
            when (value.typeOf()) {
                "long" -> DataType.NUMBER
                else -> DataType.STRING
            }

    private fun JsonObject.typeOf() = get("type").asJsonPrimitive.asString

    override fun isDatabaseWithCollections() = true

    override fun createFolder(serverConfiguration: ServerConfiguration, parentFolderName: String, folderName: String): ElasticsearchType {
        CreateType(serverConfiguration.serverUrl, parentFolderName, folderName).execute()
        return ElasticsearchType(folderName, parentFolderName, (serverConfiguration as ElasticsearchServerConfiguration).version)
    }

    override fun importFile(context: DatabaseContext, file: File): ImportResultState {
        val result = BulkImport(context.serverConfiguration.serverUrl, file).execute()
        return ImportResultState(false, "")
    }

    companion object {
        fun getInstance(project: Project) = ServiceManager.getService(project, ElasticsearchClient::class.java)
    }
}
