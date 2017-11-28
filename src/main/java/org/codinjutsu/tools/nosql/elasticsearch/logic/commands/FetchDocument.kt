package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext

internal class FetchDocument(private val context: ElasticsearchContext, private val id: String) : AbstractElasticsearchCommand() {

    override fun execute() = execute(urlWithIndexAndType(context).addNameToPath(id))
}