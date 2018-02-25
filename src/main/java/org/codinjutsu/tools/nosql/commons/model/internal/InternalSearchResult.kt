package org.codinjutsu.tools.nosql.commons.model.internal

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.model.internal.json.JsonDatabaseObjectWrapper
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class InternalSearchResult(private val delegate: SearchResult) : SearchResult by delegate {

    override val records: List<ObjectWrapper>
        get() = delegate.records
                .map {
                    JsonDatabaseObjectWrapper(it)
                }
}