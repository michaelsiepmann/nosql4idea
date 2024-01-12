package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.JsonObject
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPostCommand
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext
import java.nio.charset.StandardCharsets.UTF_8

internal class Insert(
    private val context: ElasticsearchContext,
    private val document: JsonObject,
    private val id: String?
) : AbstractPostCommand() {

    override fun buildURL() = urlWithIndexAndType(context).addNameToPath(id)

    override fun preparePost(method: HttpPost) {
        method.entity = StringEntity(document.toString(), UTF_8)
    }
}