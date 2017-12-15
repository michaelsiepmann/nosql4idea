package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.GetMethod
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext
import java.net.URLEncoder

internal abstract class AbstractElasticsearchCommand : ElasticsearchCommand {

    fun execute(url: String): JsonObject {
        val client = createClient()
        val method = createMethod(url)
        try {
            client.executeMethod(method)
            return Gson().fromJson<JsonObject>(method.responseBodyAsString, JsonObject::class.java)
        } finally {
            method.releaseConnection()
        }
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
                "${withAmp()}$key=${value.encode()}"
            } else {
                this
            }

    protected fun String.prependQuestionMark() =
            if (isNotEmpty()) {
                "?$this"
            } else {
                ""
            }

    protected fun urlWithIndexAndType(context: ElasticsearchContext): String {
        return context.serverConfiguration.serverUrl
                .addNameToPath(context.database.name)
                .addNameToPath(context.type?.name)
    }

    private fun String.withAmp() =
            if (isNotEmpty()) {
                "$this&"
            } else {
                ""
            }

    private fun String.encode(): String = URLEncoder.encode(this.removeLineBreaks(), "UTF-8")

    private fun String.removeLineBreaks() = this.replace("\n", "").replace("\r", "")
}