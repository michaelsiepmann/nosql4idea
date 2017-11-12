package org.codinjutsu.tools.nosql.elasticsearch.logic

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.FolderDatabaseClient
import org.codinjutsu.tools.nosql.commons.logic.LoadableDatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetIndices
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.GetTypes
import org.codinjutsu.tools.nosql.elasticsearch.logic.commands.Search
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchQuery
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult
import java.net.URL

internal class ElasticsearchClient : LoadableDatabaseClient<ElasticsearchResult, ElasticsearchQuery>, FolderDatabaseClient<ElasticsearchDatabase, ElasticsearchCollection> {

    override fun connect(serverConfiguration: ServerConfiguration) {
        URL(serverConfiguration.serverUrl).openConnection().connect()
    }

    override fun loadServer(databaseServer: DatabaseServer) {
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

    override fun registerServer(databaseServer: DatabaseServer?) {
    }

    override fun defaultConfiguration() =
            ServerConfiguration(serverUrl = "localhost", databaseVendor = DatabaseVendor.ELASTICSEARCH)

    override fun loadRecords(configuration: ServerConfiguration, database: Database, query: ElasticsearchQuery): ElasticsearchResult {
        val elasticsearchResult = ElasticsearchResult(database.name)
        elasticsearchResult.addAll(Search(configuration.serverUrl!!, database.name, query).execute().getAsJsonObject("hits").getAsJsonArray("hits"))
        return elasticsearchResult
    }

    override fun dropFolder(serverConfiguration: ServerConfiguration, collection: ElasticsearchCollection) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dropDatabase(serverConfiguration: ServerConfiguration, database: ElasticsearchDatabase) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getTypes(configuration: ServerConfiguration, index: String): Collection<ElasticsearchCollection> {
        return GetTypes(configuration.serverUrl!!, index)
                .execute()
                .getAsJsonObject(index)
                .getAsJsonObject("mappings")
                .entrySet()
                .map { ElasticsearchCollection(it.key, index) }
    }

    companion object {
        fun getInstance(project: Project): ElasticsearchClient = ServiceManager.getService(project, ElasticsearchClient::class.java)
    }
}