package org.codinjutsu.tools.nosql.commons.model.internal.layer.impl

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import java.util.*

internal class DatabasePrimitiveImpl(private val value: Any?) : DatabaseElementImpl(), DatabasePrimitive {

    override fun isBoolean() =
            if (value is DatabasePrimitive) {
                value.isBoolean()
            } else {
                value is Boolean
            }

    override fun isNumber() =
            if (value is DatabasePrimitive) {
                value.isNumber()
            } else {
                value is Number
            }

    override fun isDate() =
            if (value is DatabasePrimitive) {
                value.isDate()
            } else {
                value is Date
            }

    override fun isString() =
            if (value is DatabasePrimitive) {
                value.isString()
            } else {
                value is String
            }

    override fun asBoolean() =
            if (value is DatabasePrimitive) {
                value.asBoolean()
            } else {
                value as Boolean
            }

    override fun asNumber() =
            if (value is DatabasePrimitive) {
                value.asNumber()
            } else {
                value as Number
            }

    override fun asDate() =
            if (value is DatabasePrimitive) {
                value.asDate()
            } else {
                value as Date
            }

    override fun asString() =
            if (value is DatabasePrimitive) {
                value.asString()
            } else {
                value.toString()
            }

    override fun value() = value

    override fun toString() = value.toString()
}