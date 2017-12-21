package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import com.google.gson.JsonObject
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractPostCommand
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext

internal class Insert(private val context: ElasticsearchContext, private val document: JsonObject, private val id: String?) : AbstractPostCommand() {

    override fun buildURL() = urlWithIndexAndType(context).addNameToPath(id)

    override fun preparePost(method: PostMethod) {
        method.requestEntity = StringRequestEntity(document.toString(), null, null)
    }
}