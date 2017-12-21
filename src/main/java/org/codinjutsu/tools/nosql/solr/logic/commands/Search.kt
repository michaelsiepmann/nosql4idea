package org.codinjutsu.tools.nosql.solr.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.solr.model.SolrContext

internal class Search(private val context: SolrContext, private val query: QueryOptions) : AbstractGetCommand() {

    override fun buildURL() = urlWithCollection(context)
            .addNameToPath("select") + addQueryParameters()

    private fun addQueryParameters(): String {
        return "".appendQueryParameter("q", query.filter ?: "*.*")
                //.appendQueryParameter("from", query.page?.pageIndex)
                //.appendQueryParameter("size", query.page?.pageSize)
                .prependQuestionMark()
    }
}