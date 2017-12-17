package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.commons.httpclient.methods.PutMethod

abstract internal class AbstractPutCommand : AbstractHttpClientCommand() {
    override fun createMethod(url: String) = PutMethod(url)
}