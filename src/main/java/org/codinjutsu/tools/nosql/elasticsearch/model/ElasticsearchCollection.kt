package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

internal class ElasticsearchCollection(override val name: String, val databaseName: String) : Comparable<ElasticsearchCollection>, NoSQLCollection {

    override fun toString() = name

    override fun compareTo(other: ElasticsearchCollection) = this.name.compareTo(other.name)

    override fun canBeDeleted() = false // todo: This is only true from ES 2.0 and above
}
