package org.codinjutsu.tools.nosql.solr.logic

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.common.SolrDocument
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.ConfigurationException
import org.codinjutsu.tools.nosql.commons.logic.LoadableDatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.solr.configuration.SolrServerConfiguration
import org.codinjutsu.tools.nosql.solr.model.SolrResult
import org.codinjutsu.tools.nosql.solr.view.SolrContext

internal class SolrClient : LoadableDatabaseClient<SolrContext, SolrResult, SolrDocument> {

    override fun connect(serverConfiguration: ServerConfiguration) {
        try {
            createSolrClient(serverConfiguration).ping()
        } catch (e: Exception) {
            throw ConfigurationException(e)
        }
    }

    override fun loadServer(databaseServer: DatabaseServer) {
        createSolrClient(databaseServer.configuration)
    }

    override fun cleanUpServers() {
    }

    override fun registerServer(databaseServer: DatabaseServer?) {
    }

    override fun defaultConfiguration() = SolrServerConfiguration()

    override fun findDocument(context: SolrContext, _id: Any): SolrDocument? {
        return createSolrClient(context.serverConfiguration).getById(_id.toString())
    }

    override fun update(context: SolrContext, document: SolrDocument) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadRecords(context: SolrContext, query: QueryOptions?): SolrResult {
        val client = createSolrClient(context.serverConfiguration)
        val solrQuery = SolrQuery()
        solrQuery.query = query?.filter
        val response = client.query(solrQuery)
        val results = response.results
        val result = SolrResult(context.solrDatabase.name)
        results.stream()
                .forEach { result.add(it) }
        return result
    }

    override fun delete(context: SolrContext, _id: Any) {
        createSolrClient(context.serverConfiguration).deleteById(_id.toString())
    }

    private fun createSolrClient(configuration: ServerConfiguration): HttpSolrClient =
            if (configuration is SolrServerConfiguration) {
                HttpSolrClient.Builder(configuration.serverUrl).build()
            } else {
                throw IllegalStateException("Configuration must be an instance of SolrServerConfiguration")
            }

    companion object {
        fun instance(project: Project): SolrClient {
            return ServiceManager.getService(project, SolrClient::class.java)
        }
    }
}
