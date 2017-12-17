package org.codinjutsu.tools.nosql.solr.model

import org.apache.solr.common.SolrDocument
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class SolrObjectWrapper(val document: SolrDocument) : ObjectWrapper {

    override val names: Collection<String>
        get() = document.keys

    override fun get(name: String) = document[name]

    override fun isArray(value: Any?): Boolean {
        return false
    }

    override fun isObject(value: Any?): Boolean {
        return false
    }
}