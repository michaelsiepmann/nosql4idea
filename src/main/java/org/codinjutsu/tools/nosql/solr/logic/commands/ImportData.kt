package org.codinjutsu.tools.nosql.solr.logic.commands

import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPostCommand
import org.codinjutsu.tools.nosql.solr.view.SolrContext
import java.io.File

internal class ImportData(private val context: SolrContext, private val file: File) : AbstractPostCommand() {
    override fun buildURL() = urlWithCollection(context).addNameToPath("update") + addQueryParameters()

    private fun addQueryParameters() =
            "".appendQueryParameter("stream.contentType", "text/xml")
                    .prependQuestionMark()

    override fun preparePost(method: PostMethod) {
        method.addRequestHeader("Content-Type", "application/json")
        method.requestEntity = StringRequestEntity(file.readText(), null, null)
    }
}