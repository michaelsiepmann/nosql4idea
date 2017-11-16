package org.codinjutsu.tools.nosql.commons.view.panel.query

internal data class QueryOptions(
        var operations: String? = null,
        var resultLimit: Int = 300,
        var filter: String? = null,
        var projection: String? = null,
        var sort: String? = null
)