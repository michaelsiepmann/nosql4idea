package org.codinjutsu.tools.nosql.elasticsearch.view.panel.query

import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions

internal data class ElasticsearchQueryOptions(
        override val resultLimit: Int,
        override val filter: String?,
        override val page: Page?,
        override val operations: String? = null,
        override val projection: String? = null,
        override val sort: String? = null
) : QueryOptions