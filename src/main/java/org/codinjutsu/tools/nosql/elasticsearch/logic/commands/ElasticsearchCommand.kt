package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.JsonObject
import org.apache.commons.httpclient.HttpClient

internal interface ElasticsearchCommand {

    fun execute() : JsonObject

    fun createClient() = HttpClient()
}