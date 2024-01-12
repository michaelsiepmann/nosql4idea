package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.http.client.methods.HttpGet

abstract internal class AbstractGetCommand : AbstractHttpClientCommand() {
    override fun createMethod(url: String) = HttpGet(url)
}