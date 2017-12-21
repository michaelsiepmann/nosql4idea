package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.NamedObject

internal class ElasticsearchType(override val name: String, val databaseName: String, val version: ElasticsearchVersion) : NamedObject, Comparable<ElasticsearchType> {

    override fun toString() = name

    override fun compareTo(other: ElasticsearchType) = this.name.compareTo(other.name)
}
