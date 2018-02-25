package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseObject : DatabaseElement {

    override fun isObject() = true

    fun names() : Collection<String>

    fun get(key: String): DatabaseElement?

    fun add(key: String, value: Any?) {
        throw UnsupportedOperationException("This is not supported")
    }

    fun addProperty(key: String, value: Boolean) {
        throw UnsupportedOperationException("This is not supported")
    }

    fun addProperty(key: String, value: Char) {
        throw UnsupportedOperationException("This is not supported")
    }

    fun addProperty(key: String, value: Int) {
        throw UnsupportedOperationException("This is not supported")
    }

    fun addProperty(key: String, value: String) {
        throw UnsupportedOperationException("This is not supported")
    }
}