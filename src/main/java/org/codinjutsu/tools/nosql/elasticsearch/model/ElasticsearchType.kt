package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

internal class ElasticsearchType(override val name: String, val databaseName: String, val version : ElasticsearchVersion) : Comparable<ElasticsearchType>, NoSQLCollection {

    override fun toString() = name

    override fun compareTo(other: ElasticsearchType) = this.name.compareTo(other.name)
}
