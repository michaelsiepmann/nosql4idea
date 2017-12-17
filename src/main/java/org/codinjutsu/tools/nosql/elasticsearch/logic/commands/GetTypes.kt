package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand

internal class GetTypes(val url: String, val index: String) : AbstractGetCommand() {

    override fun buildURL() = "$url/$index"
}