package org.codinjutsu.tools.nosql.elasticsearch.logic

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.LoadableDatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.Query
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult

internal class ElasticsearchClient : LoadableDatabaseClient<ElasticsearchResult> {

    override fun connect(serverConfiguration: ServerConfiguration) {
    }

    override fun loadServer(databaseServer: DatabaseServer) {
        val client = HttpClient()
        val method = GetMethod("${databaseServer.configuration.serverUrl}/_indices")
        try {
            if (client.executeMethod(method) == 200) {
                val databases = mutableListOf<Database>()

                databaseServer.databases.addAll(databases)
            }
        } finally {
            method.releaseConnection()
        }
    }

    override fun cleanUpServers() {
    }

    override fun registerServer(databaseServer: DatabaseServer?) {
    }

    override fun defaultConfiguration() =
            ServerConfiguration(serverUrl = "localhost", databaseVendor = DatabaseVendor.ELASTICSEARCH)

    override fun loadRecords(configuration: ServerConfiguration?, database: Database?, couchbaseQuery: Query?): ElasticsearchResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun getInstance(project: Project) = ServiceManager.getService(project, ElasticsearchClient::class.java)
    }
}