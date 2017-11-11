package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

internal class ElasticsearchCollection(override val name: String, val databaseName: String) : Comparable<ElasticsearchCollection>, NoSQLCollection {

    override fun toString() = name

    override fun compareTo(other: ElasticsearchCollection): Int {
        return this.name.compareTo(other.name)
    }
}
