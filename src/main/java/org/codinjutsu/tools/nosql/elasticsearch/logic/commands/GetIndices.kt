package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand

internal open class GetIndices(private val url: String) : AbstractGetCommand() {

    override fun buildURL() = "$url/_aliases"
}