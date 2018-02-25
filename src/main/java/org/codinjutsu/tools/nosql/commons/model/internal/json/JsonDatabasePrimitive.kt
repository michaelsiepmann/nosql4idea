package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.google.gson.JsonPrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive

internal class JsonDatabasePrimitive(internal val jsonPrimitive: JsonPrimitive) : DatabasePrimitive {

    override fun isBoolean() = jsonPrimitive.isBoolean

    override fun isNumber() = jsonPrimitive.isNumber

    override fun isString() = jsonPrimitive.isString

    override fun asBoolean() = jsonPrimitive.asBoolean

    override fun asNumber() = jsonPrimitive.asNumber

    override fun asString() = jsonPrimitive.asString

    override fun value(): Any? {
        return when {
            jsonPrimitive.isBoolean -> jsonPrimitive.asBoolean
            jsonPrimitive.isNumber -> jsonPrimitive.asNumber
            else -> jsonPrimitive.asString
        }
    }

    override fun toString() = jsonPrimitive.toString()
}