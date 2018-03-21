package org.codinjutsu.tools.nosql.commons.model.internal.layer.impl

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

internal class DatabaseArrayImpl : DatabaseElementImpl(), DatabaseArray {

    private val list = mutableListOf<DatabaseElement>()

    override fun size() = list.size

    override fun get(index: Int) = list[index]

    override fun iterator() = list.iterator()

    fun add(element: DatabaseElement) {
        list.add(element)
    }

    override fun toString() = list.toString()
}