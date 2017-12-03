package org.codinjutsu.tools.nosql.elasticsearch.model

internal class ElasticsearchType(val name: String, val databaseName: String, val version: ElasticsearchVersion) : Comparable<ElasticsearchType> {

    override fun toString() = name

    override fun compareTo(other: ElasticsearchType) = this.name.compareTo(other.name)
}
