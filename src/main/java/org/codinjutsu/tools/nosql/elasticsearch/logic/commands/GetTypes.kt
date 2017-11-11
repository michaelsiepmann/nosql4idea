package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

internal class GetTypes(val url: String, val index: String) : AbstractElasticsearchCommand() {

    override fun execute() = execute("$url/$index")
}