package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractDeleteCommand

internal open class DeleteElement(private val url: String) : AbstractDeleteCommand() {

    override fun buildURL() = url
}