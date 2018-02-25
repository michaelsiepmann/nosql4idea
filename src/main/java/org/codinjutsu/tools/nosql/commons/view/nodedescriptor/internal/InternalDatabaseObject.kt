package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal class InternalDatabaseObject : InternalDatabaseElement(), DatabaseObject {

    private val map = mutableMapOf<String, InternalDatabaseElement>()

    override fun names() = map.keys

    override fun get(key: String) = map[key]

    override fun add(key: String, value: Any?) {
        map[key] = InternalDatabasePrimitive(value)
    }

    override fun addProperty(key: String, value: Boolean) {
        map[key] = InternalDatabasePrimitive(value)
    }

    override fun addProperty(key: String, value: Char) {
        map[key] = InternalDatabasePrimitive(value)
    }

    override fun addProperty(key: String, value: Int) {
        map[key] = InternalDatabasePrimitive(value)
    }

    override fun addProperty(key: String, value: String) {
        map[key] = InternalDatabasePrimitive(value)
    }

    override fun toString() = map.toString()
}