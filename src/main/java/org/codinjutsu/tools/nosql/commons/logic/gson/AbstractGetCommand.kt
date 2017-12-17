package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.commons.httpclient.methods.GetMethod

abstract internal class AbstractGetCommand : AbstractHttpClientCommand() {
    override fun createMethod(url: String) = GetMethod(url)
}