package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

internal open class GetIndices(private val url: String) : AbstractElasticsearchCommand() {

    override fun execute() = execute("$url/_aliases")
}