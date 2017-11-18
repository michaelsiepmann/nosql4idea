package org.codinjutsu.tools.nosql.commons.view.wrapper

interface ObjectWrapper {
    val names: Collection<String>

    fun get(name: String): Any?

    fun isArray(value: Any?): Boolean

    fun isObject(value: Any?): Boolean
}