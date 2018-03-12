package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal data class SearchResult(
        val name: String,
        val records: List<DatabaseObject>,
        val totalCount: Int
)
