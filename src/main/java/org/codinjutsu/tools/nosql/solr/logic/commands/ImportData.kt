package org.codinjutsu.tools.nosql.solr.logic.commands

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPostCommand
import org.codinjutsu.tools.nosql.solr.model.SolrContext
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

internal class ImportData(private val context: SolrContext, private val file: File) : AbstractPostCommand() {
    override fun buildURL() = urlWithCollection(context).addNameToPath("update") + addQueryParameters()

    private fun addQueryParameters() =
        "".appendQueryParameter("stream.contentType", "text/xml")
            .prependQuestionMark()

    override fun preparePost(method: HttpPost) {
        method.addHeader("Content-Type", "application/json")
        method.entity = StringEntity(file.readText(), UTF_8)
    }
}