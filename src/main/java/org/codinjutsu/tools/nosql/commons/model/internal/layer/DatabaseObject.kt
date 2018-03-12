package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseObject : DatabaseElement {

    override fun isObject() = true

    fun names(): Collection<String>

    operator fun get(key: String): DatabaseElement?

    fun getAsDatabaseObject(key: String) = get(key) as? DatabaseObject

    fun getAsDatabaseArray(key: String) = get(key) as? DatabaseArray

    fun getAsDatabasePrimitive(key: String) = get(key) as? DatabasePrimitive

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

    fun toMap(): Map<String, DatabaseElement?> {
        val result: MutableMap<String, DatabaseElement?> = mutableMapOf()
        names().forEach {
            result[it] = get(it)
        }
        return result
    }
}