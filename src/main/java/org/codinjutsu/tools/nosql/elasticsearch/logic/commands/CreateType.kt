package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPutCommand

internal class CreateType(private val url: String, private val index: String, private val type: String) : AbstractPutCommand() {

    override fun buildURL() = url.addNameToPath(index).addNameToPath(type)
}