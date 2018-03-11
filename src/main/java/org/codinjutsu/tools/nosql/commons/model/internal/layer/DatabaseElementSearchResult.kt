package org.codinjutsu.tools.nosql.commons.model.internal.layer

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class DatabaseElementSearchResult(
        override val name: String,
        override val records: List<ObjectWrapper>,
        override val resultCount: Int
) : SearchResult