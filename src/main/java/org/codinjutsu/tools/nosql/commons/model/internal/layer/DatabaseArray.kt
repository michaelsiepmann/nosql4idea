package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseArray : DatabaseElement, Iterable<DatabaseElement> {

    override fun isArray() = true

    fun size(): Int

    operator fun get(index: Int): DatabaseElement?
}