package org.codinjutsu.tools.nosql.commons.logic.gson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.apache.commons.io.IOUtils
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8

internal abstract class AbstractHttpClientCommand : HttpClientCommand {

    override fun execute() = execute(buildURL())

    protected abstract fun buildURL(): String

    private fun execute(url: String): JsonObject {
        val client = createClient()
        val method = createMethod(url)
        try {
            val response = client.execute(method)
            val content = IOUtils.toString(response.entity.content, UTF_8)
            val result = Gson().fromJson<JsonElement>(content, JsonElement::class.java)
            if (result is JsonObject) {
                return result
            }
            val jsonObject = JsonObject()
            jsonObject.add("result", result)
            return jsonObject
        } catch (e: Exception) {
            val message = "Error while fetching from URL $url with method ${method.method}."
//            log.error(message, e)
            throw ClientCommandException(message, e)
        } finally {
            if (client is CloseableHttpClient) {
                client.close()
            }
        }
    }

    protected abstract fun createMethod(url: String): HttpUriRequest

    open protected fun createClient(): HttpClient {
        return HttpClientBuilder.create().build()
    }

    internal fun String.addNameToPath(name: String?) =
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


    private fun String.withAmp() =
        if (isNotEmpty()) {
            "$this&"
        } else {
            ""
        }

    private fun String.encode(): String = URLEncoder.encode(this.removeLineBreaks(), "UTF-8")

    private fun String.removeLineBreaks() = this.replace("\n", "").replace("\r", "")

    companion object {
//        private val log = getLog(AbstractHttpClientCommand::class.java)
    }
}