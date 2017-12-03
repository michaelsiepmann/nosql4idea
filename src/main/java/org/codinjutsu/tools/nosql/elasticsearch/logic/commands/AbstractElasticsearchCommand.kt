package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.GetMethod
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext

internal abstract class AbstractElasticsearchCommand : ElasticsearchCommand {

    fun execute(url: String): JsonObject {
        val client = createClient()
        val method = createMethod(url)
        try {
            val statusCode = client.executeMethod(method)
            if (statusCode in 200..299) {
                return Gson().fromJson<JsonObject>(method.responseBodyAsString, JsonObject::class.java)
            }
        } finally {
            method.releaseConnection()
        }
        return JsonObject()
    }

    protected open fun createMethod(url: String): HttpMethod = GetMethod(url)

    protected fun String.addNameToPath(name: String?) =
            if (name?.isNotEmpty() == true) {
                "$this/$name"
            } else {
                this
            }

    protected fun String.appendQueryParameter(key: String, value: Int?) =
            if (value != null) {
                appendQueryParameter(key, value.toString())
            } else {
                this
            }

    protected fun String.appendQueryParameter(key: String, value: String?) =
            if (value?.isNotEmpty() == true) {
                if (isEmpty()) {
                    "$key=$value"
                } else {
                    "$this&amp;$key=$value"
                }
            } else {
                this
            }

    protected fun String.prependQuestionMark() =
            if (isNotEmpty()) {
                "?$this"
            } else {
                ""
            }

    protected fun urlWithIndexAndType(elasticsearchContext: ElasticsearchContext): String {
        return elasticsearchContext.serverConfiguration.serverUrl!!
                .addNameToPath(elasticsearchContext.database.name)
                .addNameToPath(elasticsearchContext.type?.name)
    }
}