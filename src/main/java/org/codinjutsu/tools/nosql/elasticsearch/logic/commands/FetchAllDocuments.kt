package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext

internal class FetchAllDocuments(private val context: ElasticsearchContext) : AbstractGetCommand() {

    override fun buildURL() = urlWithIndexAndType(context).addNameToPath("_search?")
}