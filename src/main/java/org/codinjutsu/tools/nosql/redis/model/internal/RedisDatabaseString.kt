package org.codinjutsu.tools.nosql.redis.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import java.util.*

internal class RedisDatabaseString(private val value: String) : DatabasePrimitive {

    override fun isBoolean() = false

    override fun isNumber() = false

    override fun isDate() = false

    override fun isString() = true

    override fun asBoolean(): Boolean {
        throw IllegalStateException("This is no boolean")
    }

    override fun asNumber(): Number {
        throw IllegalStateException("This is no number")
    }

    override fun asDate(): Date {
        throw IllegalStateException("This is no date")
    }

    override fun asString() = value

    override fun value() = value
}