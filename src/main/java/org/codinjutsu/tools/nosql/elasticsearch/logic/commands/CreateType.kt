package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.methods.PutMethod

internal class CreateType(private val url: String, private val index: String, private val type: String) : AbstractElasticsearchCommand() {

    override fun execute() = execute(buildUrl())

    private fun buildUrl() = url.addNameToPath(index).addNameToPath(type)

    override fun createMethod(url: String) = PutMethod()
}