package org.codinjutsu.tools.nosql.solr.model

import org.apache.solr.common.SolrDocument
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper
import java.util.*


class SolrResult(override val name: String) : SearchResult {

    private val jsonObjects = LinkedList<ObjectWrapper>()

    override val records: List<ObjectWrapper>
        get() = jsonObjects

    fun add(document: SolrDocument) {
        jsonObjects.add(SolrObjectWrapper(document))
    }
}