package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest

internal abstract class AbstractPostCommand : AbstractHttpClientCommand() {

    override fun createMethod(url: String): HttpUriRequest {
        val method = HttpPost(url)
        preparePost(method)
        return method
    }

    protected abstract fun preparePost(method: HttpPost)
}