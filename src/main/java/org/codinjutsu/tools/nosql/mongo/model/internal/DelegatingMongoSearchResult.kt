package org.codinjutsu.tools.nosql.mongo.model.internal

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class DelegatingMongoSearchResult(private val searchResult: SearchResult) : SearchResult {
    override val name: String
        get() = searchResult.name
    override val records: List<ObjectWrapper>
        get() = searchResult.records
                .map { DelegatingMongoObjectWrapper(it) }
}