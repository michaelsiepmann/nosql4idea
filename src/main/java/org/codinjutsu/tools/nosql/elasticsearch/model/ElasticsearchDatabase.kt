package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.Database

internal class ElasticsearchDatabase(name: String, private val types: MutableCollection<ElasticsearchType> = mutableSetOf()) : Database(name) {
    fun getTypes() = types

    fun addType(type: ElasticsearchType) {
        types.add(type)
    }
}