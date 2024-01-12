package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

internal class FetchDocument(private val context: ElasticsearchContext, private val id: String) : AbstractGetCommand() {

    override fun buildURL() = urlWithIndexAndType(context).addNameToPath(URLEncoder.encode(id, UTF_8))
}