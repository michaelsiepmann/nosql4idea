package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPostCommand
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

internal class BulkImport(private val url: String, private val file: File) : AbstractPostCommand() {

    override fun buildURL() = "$url/_bulk"

    override fun preparePost(method: HttpPost) {
        method.entity = StringEntity(file.readText(), UTF_8)
    }
}