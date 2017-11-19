package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext

internal class Search(private val context: ElasticsearchContext) : AbstractElasticsearchCommand() {

    override fun execute() = execute(buildURL())

    private fun buildURL() = "${context.serverConfiguration.serverUrl}/${context.database.name}" + addNameToPath(context.collection?.name) + "/_search"

    private fun addNameToPath(name: String?) = if (name != null) {
        "/$name"
    } else {
        ""
    }
}