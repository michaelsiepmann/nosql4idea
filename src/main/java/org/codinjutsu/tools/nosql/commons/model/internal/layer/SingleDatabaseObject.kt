package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal class SingleDatabaseObject(private val names: Collection<String>, private val values: Map<String, DatabaseElement?>) : DatabaseObject {
    override fun names() = names

    override fun get(key: String) = values[key]
}