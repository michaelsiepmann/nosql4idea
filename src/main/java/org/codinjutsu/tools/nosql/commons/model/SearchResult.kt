package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal data class SearchResult(
        val name: String,
        val records: List<ObjectWrapper>,
        val totalCount: Int
)
