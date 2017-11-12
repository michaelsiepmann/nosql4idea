package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchQuery

internal class Search(private val url: String, private val databaseName: String, private val query: ElasticsearchQuery) : AbstractElasticsearchCommand() {

    override fun execute() = execute(buildURL())

    private fun buildURL() = "$url/$databaseName" + addNameToPath(query.collection?.name) + "/_search"

    private fun addNameToPath(name: String?) = if (name != null) {
        "/$name"
    } else {
        ""
    }
}