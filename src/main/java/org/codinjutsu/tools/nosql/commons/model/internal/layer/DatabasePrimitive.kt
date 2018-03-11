package org.codinjutsu.tools.nosql.commons.model.internal.layer

import java.util.*

internal interface DatabasePrimitive : DatabaseElement {

    override fun isPrimitive() = true

    fun isBoolean(): Boolean

    fun isNumber(): Boolean

    fun isDate(): Boolean

    fun isString(): Boolean

    fun asBoolean(): Boolean

    override fun asInt(): Int {
        return asNumber().toInt()
    }

    fun asNumber(): Number

    fun asDate(): Date

    fun asString(): String

    fun value(): Any?
}