package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext

internal class Search(private val context: ElasticsearchContext, private val query: QueryOptions) : AbstractElasticsearchCommand() {

    override fun execute() = execute(buildURL())

    private fun buildURL() = urlWithIndexAndType(context) + "/_search${addQueryParameters()}"

    private fun addQueryParameters(): String {
        return "".appendQueryParameter("q", query.filter)
                .appendQueryParameter("from", query.page?.pageIndex)
                .appendQueryParameter("size", query.page?.pageSize)
                .prependQuestionMark()
    }
}