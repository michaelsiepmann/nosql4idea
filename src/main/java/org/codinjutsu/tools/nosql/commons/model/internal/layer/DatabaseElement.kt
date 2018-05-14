package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseElement {

    fun isObject(): Boolean = false

    fun isArray(): Boolean = false

    fun isPrimitive(): Boolean = false

    fun isNull(): Boolean = false

    fun asInt(): Int {
        throw IllegalStateException("Value can not be parsed as an object")
    }

    fun asObject(): DatabaseObject {
        throw IllegalStateException("Value can not be parsed as an object")
    }
}