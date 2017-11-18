package org.codinjutsu.tools.nosql.commons.view.panel.query


data class QueryOptions @JvmOverloads constructor(
        var operations: String? = null,
        var resultLimit: Int = 300,
        var filter: String? = null,
        var projection: String? = null,
        var sort: String? = null
)