package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.http.client.methods.HttpPut

abstract internal class AbstractPutCommand : AbstractHttpClientCommand() {
    override fun createMethod(url: String) = HttpPut(url)
}