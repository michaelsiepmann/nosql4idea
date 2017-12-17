package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPostCommand
import java.io.File

internal class BulkImport(private val url: String, private val file: File) : AbstractPostCommand() {

    override fun buildURL() = "$url/_bulk"

    override fun preparePost(method: PostMethod) {
        method.requestEntity = StringRequestEntity(file.readText(), null, null)
    }
}