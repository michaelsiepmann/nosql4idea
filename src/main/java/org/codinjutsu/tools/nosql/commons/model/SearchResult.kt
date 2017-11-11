package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal interface SearchResult {

    val name: String

    val records: List<ObjectWrapper>
}