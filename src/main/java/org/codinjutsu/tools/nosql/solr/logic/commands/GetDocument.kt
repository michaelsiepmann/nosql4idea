package org.codinjutsu.tools.nosql.solr.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand
import org.codinjutsu.tools.nosql.solr.model.SolrContext

internal class GetDocument(private val context: SolrContext, private val id: String) : AbstractGetCommand() {

    override fun buildURL() = urlWithCollection(context).addNameToPath("get") + "".appendQueryParameter("id", id).prependQuestionMark()
}