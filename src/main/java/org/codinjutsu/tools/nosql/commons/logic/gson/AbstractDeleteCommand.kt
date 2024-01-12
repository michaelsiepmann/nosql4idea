package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.http.client.methods.HttpDelete

abstract internal class AbstractDeleteCommand : AbstractHttpClientCommand() {
    override fun createMethod(url: String) = HttpDelete(url)
}