package org.codinjutsu.tools.nosql.commons.logic.gson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import java.net.URLEncoder

internal abstract class AbstractHttpClientCommand : HttpClientCommand {

    override fun execute() = execute(buildURL())

    protected abstract fun buildURL(): String

    private fun execute(url: String): JsonObject {
        val client = createClient()
        val method = createMethod(url)
        try {
            client.executeMethod(method)
            val result = Gson().fromJson<JsonElement>(method.responseBodyAsString, JsonElement::class.java)
            if (result is JsonObject) {
                return result
            }
            val jsonObject = JsonObject()
            jsonObject.add("result", result)
            return jsonObject
        } catch (e: Exception) {
            val message = "Error while fetching from URL $url with method ${method.name}."
//            log.error(message, e)
            throw ClientCommandException(message, e)
        } finally {
            method.releaseConnection()
        }
    }

    protected abstract fun createMethod(url: String): HttpMethod

    open protected fun createClient() = HttpClient()

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