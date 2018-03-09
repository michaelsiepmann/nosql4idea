package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.PostMethod

internal abstract class AbstractPostCommand : AbstractHttpClientCommand() {

    override fun createMethod(url: String): HttpMethod {
        val method = PostMethod(url)
        preparePost(method)
        return method
    }

    protected abstract fun preparePost(method: PostMethod)
}