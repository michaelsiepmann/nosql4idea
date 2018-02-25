package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabaseArray
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabaseElement
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabaseObject
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive

internal fun convert(value: JsonElement?) =
        when {
            value == null -> null
            value.isJsonObject -> JsonDatabaseObject(value.asJsonObject)
            value.isJsonArray -> JsonDatabaseArray(value.asJsonArray)
            else -> JsonDatabasePrimitive(value.asJsonPrimitive)
        }

internal fun revert(value: DatabaseElement?) =
        when (value) {
            null -> null
            is JsonDatabaseObject -> value.jsonObject
            is JsonDatabaseArray -> value.jsonArray
            is JsonDatabasePrimitive -> value.jsonPrimitive
            is InternalDatabaseElement -> revertInternal(value)
            else -> throw IllegalStateException("Unknown type ${value.javaClass}")
        }

private fun revertInternal(value: InternalDatabaseElement) =
        when (value) {
            is InternalDatabasePrimitive -> revertInternalPrimitive(value)
            is InternalDatabaseObject -> revertInternalObject(value)
            is InternalDatabaseArray -> revertInternalArray(value)
            else -> throw IllegalStateException("Unknown type ${value.javaClass}")
        }

private fun revertInternalObject(value: InternalDatabaseObject): JsonElement {
    val result = JsonObject()
    value.names().forEach { name: String ->
        value.get(name)?.let {
            result.add(name, revertInternal(it))
        }
    }
    return result
}

private fun revertInternalPrimitive(value: InternalDatabasePrimitive): JsonElement =
        when {
            value.isBoolean() -> JsonPrimitive(value.asBoolean())
            value.isNumber() -> JsonPrimitive(value.asNumber())
            value.isString() -> JsonPrimitive(value.asString())
            else -> {
                val internalVal = value.value()
                when (internalVal) {
                    is DatabaseElement -> revert(internalVal)!!
                    else -> JsonPrimitive(value.toString())
                }
            }
        }

private fun revertInternalArray(value: InternalDatabaseArray): JsonElement {
    val result = JsonArray()
    value.toArray().forEach {
        result.add(revert(it))
    }
    return result
}
