package org.codinjutsu.tools.nosql.elasticsearch.logic

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.logic.ConfigurationException
import org.codinjutsu.tools.nosql.commons.logic.LoadableDatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.CreateType
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.DeleteElement
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.FetchDocument
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetIndices
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetTypes
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Insert
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Search
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext
import java.net.URL

internal class ElasticsearchClient : LoadableDatabaseClient<ElasticsearchContext, ElasticsearchResult, JsonObject, ElasticsearchServerConfiguration, ElasticsearchType> {

    override fun connect(serverConfiguration: ElasticsearchServerConfiguration) {
        try {
            URL(serverConfiguration.serverUrl).openConnection().connect()
        } catch (e: Exception) {
            throw ConfigurationException(e)
        }
    }

    override fun loadServer(databaseServer: DatabaseServer<ElasticsearchServerConfiguration>) {
        val databases = GetIndices(databaseServer.configuration.serverUrl!!)
                .execute()
                .entrySet()
                .map {
                    ElasticsearchDatabase(it.key, getTypes(databaseServer.configuration, it.key))
                }
        databaseServer.databases.addAll(databases)
    }

    override fun cleanUpServers() {
    }

    override fun registerServer(databaseServer: DatabaseServer<ElasticsearchServerConfiguration>?) {
    }

    override fun defaultConfiguration() =
            ElasticsearchServerConfiguration(ElasticsearchVersion.VERSION_20, "http://localhost:9200", DatabaseVendor.ELASTICSEARCH)

    override fun loadRecords(context: ElasticsearchContext, queryOptions: QueryOptions): ElasticsearchResult {
        val elasticsearchResult = ElasticsearchResult(context.database.name)
        elasticsearchResult.addAll(Search(context, queryOptions).execute().getAsJsonObject("hits")?.getAsJsonArray("hits") ?: JsonArray())
        return elasticsearchResult
    }

    override fun dropFolder(serverConfiguration: ElasticsearchServerConfiguration, type: ElasticsearchType) {
        DeleteElement("${serverConfiguration.serverUrl!!}/${(type as ElasticsearchType).databaseName}/${type.name}").execute()
    }

    override fun dropDatabase(serverConfiguration: ElasticsearchServerConfiguration, database: Database) {
        DeleteElement("${serverConfiguration.serverUrl!!}/${database.name}").execute()
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
        val collection = context.type
        if (collection != null) {
            DeleteElement("${serverConfiguration.serverUrl!!}/${database.name}/${collection.name}/$_id").execute()
        }
    }

    private fun getTypes(configuration: ElasticsearchServerConfiguration, index: String): MutableCollection<ElasticsearchType> {
        return GetTypes(configuration.serverUrl!!, index)
                .execute()
                .getAsJsonObject(index)
                .getAsJsonObject("mappings")
                .entrySet()
                .map { ElasticsearchType(it.key, index, configuration.version) }
                .toMutableList()
    }

    override fun isDatabaseWithCollections() = true

    override fun createFolder(serverConfiguration: ElasticsearchServerConfiguration, parentFolderName: String, folderName: String): ElasticsearchType {
        CreateType(serverConfiguration.serverUrl!!, parentFolderName, folderName).execute()
        return ElasticsearchType(folderName, parentFolderName, serverConfiguration.version)
    }

    companion object {
        fun getInstance(project: Project): ElasticsearchClient = ServiceManager.getService(project, ElasticsearchClient::class.java)
    }
}