package org.codinjutsu.tools.nosql.commons.logic.gson

import org.apache.commons.httpclient.methods.DeleteMethod

abstract internal class AbstractDeleteCommand : AbstractHttpClientCommand() {
    override fun createMethod(url: String) = DeleteMethod(url)
}