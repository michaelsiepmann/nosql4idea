package org.codinjutsu.tools.nosql.commons.view.panel.query

internal data class QueryOptionsImpl @JvmOverloads constructor(
        override var operations: String? = null,
        override var resultLimit: Int = 300,
        override var filter: String? = null,
        override var projection: String? = null,
        override var sort: String? = null,
        override var page: Page? = null
) : QueryOptions