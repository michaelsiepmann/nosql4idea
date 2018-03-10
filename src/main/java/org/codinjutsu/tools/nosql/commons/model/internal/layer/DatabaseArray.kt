package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseArray : DatabaseElement, Iterable<DatabaseElement> {

    override fun isArray() = true

    fun size(): Int

    fun get(index: Int): DatabaseElement?

    fun toArray(): Iterator<DatabaseElement>

    override fun iterator(): Iterator<DatabaseElement> {
        return toArray()
    }
}