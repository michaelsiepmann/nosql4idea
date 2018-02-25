package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

internal class InternalDatabaseArray : InternalDatabaseElement(), DatabaseArray {

    private val list = mutableListOf<DatabaseElement>()

    override fun size() = list.size

    override fun get(index: Int) = list[index]

    override fun toArray() = list.iterator()

    fun add(element: DatabaseElement) {
        list.add(element)
    }

    override fun toString() = list.toString()
}