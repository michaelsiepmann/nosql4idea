package org.codinjutsu.tools.nosql.commons.model.internal.layer.impl

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal class DatabaseObjectImpl : DatabaseElementImpl(), DatabaseObject {

    private val map = mutableMapOf<String, DatabaseElementImpl>()

    override fun names() = map.keys

    override fun get(key: String) = map[key]

    override fun add(key: String, value: Any?) {
        if (value is DatabaseElementImpl) {
            map[key] = value
        } else {
            map[key] = DatabasePrimitiveImpl(value)
        }
    }

    override fun addProperty(key: String, value: Boolean) {
        map[key] = DatabasePrimitiveImpl(value)
    }

    override fun addProperty(key: String, value: Char) {
        map[key] = DatabasePrimitiveImpl(value)
    }

    override fun addProperty(key: String, value: Int) {
        map[key] = DatabasePrimitiveImpl(value)
    }

    override fun addProperty(key: String, value: String) {
        map[key] = DatabasePrimitiveImpl(value)
    }

    override fun asObject() = this

    override fun toString() = map.toString()
}