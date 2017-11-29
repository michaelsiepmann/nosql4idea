package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

interface SearchResult {

    val name: String
    val records: List<ObjectWrapper>
    val count: Int
        get() = records.size
}
