package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal class DatabaseArrayStub(private val children: List<DatabaseElement> = emptyList()) : DatabaseArray {

    constructor(vararg element: DatabaseElement) : this(element.toList())

    override fun size() = children.size

    override fun get(index: Int) = children[index]

    override fun iterator() = children.iterator()
}