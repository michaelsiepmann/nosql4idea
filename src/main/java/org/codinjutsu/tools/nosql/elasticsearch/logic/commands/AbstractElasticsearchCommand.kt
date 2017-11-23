package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.GetMethod

internal abstract class AbstractElasticsearchCommand : ElasticsearchCommand {

    fun execute(url: String): JsonObject {
        val client = createClient()
        val method = createMethod(url)
        try {
            if (client.executeMethod(method) == 200) {
                return Gson().fromJson<JsonObject>(method.responseBodyAsString, JsonObject::class.java)
            }
        } finally {
            method.releaseConnection()
        }
        return JsonObject()
    }

    protected open fun createMethod(url: String): HttpMethod = GetMethod(url)
}