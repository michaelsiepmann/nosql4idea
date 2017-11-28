package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.methods.DeleteMethod

internal open class DeleteElement(private val url: String) : AbstractElasticsearchCommand() {

    override fun execute() = execute(url)

    override fun createMethod(url: String) = DeleteMethod(url)
}