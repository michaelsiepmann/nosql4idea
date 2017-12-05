package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import java.io.File

internal class BulkImport(private val url: String, private val file: File) : AbstractElasticsearchCommand() {

    override fun execute() = execute("$url/_bulk")

    override fun createMethod(url: String): HttpMethod {
        val method = PostMethod(url)
        method.requestEntity = StringRequestEntity(file.readText(), null, null)
        return method
    }
}