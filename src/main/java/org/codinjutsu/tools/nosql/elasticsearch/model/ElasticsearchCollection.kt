package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

internal class ElasticsearchCollection(override val name: String, val databaseName: String, val version : ElasticsearchVersion) : Comparable<ElasticsearchCollection>, NoSQLCollection {

    override fun toString() = name

    override fun compareTo(other: ElasticsearchCollection) = this.name.compareTo(other.name)

    override fun canBeDeleted() = version >= ElasticsearchVersion.VERSION_20
}
