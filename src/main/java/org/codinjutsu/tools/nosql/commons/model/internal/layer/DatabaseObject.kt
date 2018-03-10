package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseObject : DatabaseElement {

    override fun isObject() = true

    fun names(): Collection<String>

    fun get(key: String): DatabaseElement?

    fun getAsDatabaseObject(key: String): DatabaseObject? {
        val result = get(key)
        return if (result is DatabaseObject) {
            result
        } else {
            null
        }
    }

    fun getAsDatabaseArray(key: String): DatabaseArray? {
        val result = get(key)
        return if (result is DatabaseArray) {
            result
        } else {
            null
        }
    }

    fun getAsDatabasePrimitive(key: String): DatabasePrimitive? {
        val result = get(key)
        return if (result is DatabasePrimitive) {
            result
        } else {
            null
        }
    }

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