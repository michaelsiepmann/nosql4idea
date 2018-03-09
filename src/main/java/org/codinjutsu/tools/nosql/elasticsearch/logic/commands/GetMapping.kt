package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand

internal class GetMapping(private val url: String, private val index: String, private val type: String) : AbstractGetCommand() {

    override fun buildURL() = url.addNameToPath(index)
            .addNameToPath("_mappings")
            .addNameToPath(type)
}