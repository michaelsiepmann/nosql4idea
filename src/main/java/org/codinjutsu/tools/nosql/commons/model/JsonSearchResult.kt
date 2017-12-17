package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class JsonSearchResult(
        override val name: String,
        override val records: List<ObjectWrapper>,
        override val resultCount: Int
) : SearchResult