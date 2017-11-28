package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.JsonObject
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext

internal class Insert(private val context: ElasticsearchContext, private val document: JsonObject) : AbstractElasticsearchCommand() {

    override fun execute() = execute(urlWithIndexAndType(context))

    override fun createMethod(url: String): HttpMethod {
        val method = PostMethod(url)
        method.requestEntity = StringRequestEntity(document.toString(), null, null)
        return method
    }
}