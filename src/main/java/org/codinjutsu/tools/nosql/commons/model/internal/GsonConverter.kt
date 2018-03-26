package org.codinjutsu.tools.nosql.commons.model.internal

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseArrayImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseObjectImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl

internal fun JsonElement.toDatabaseElement(): DatabaseElement {
    return when (this) {
        is JsonObject -> toDatabaseElement()
        is JsonArray -> toDatabaseElement()
        is JsonPrimitive -> toDatabaseElement()
        else -> DatabasePrimitiveImpl(asString)
    }
}

internal fun JsonObject.toDatabaseElement(): DatabaseObject {
    val result = DatabaseObjectImpl()
    keySet().forEach {
        result.add(it, get(it).toDatabaseElement())
    }
    return result
}

internal fun JsonArray.toDatabaseElement(): DatabaseArray {
    val result = DatabaseArrayImpl()
    (0 until size()).forEach {
        result.add(get(it).toDatabaseElement())
    }
    return result
}

internal fun JsonPrimitive.toDatabaseElement(): DatabasePrimitive {
    return when {
        isBoolean -> DatabasePrimitiveImpl(asBoolean)
        isNumber -> DatabasePrimitiveImpl(asNumber)
        else -> DatabasePrimitiveImpl(asString)
    }
}

internal fun DatabaseElement.toJsonElement(): JsonElement {
    return when (this) {
        is DatabaseObject -> toJsonElement()
        is DatabaseArray -> toJsonElement()
        is DatabasePrimitive -> toJsonElement()
        else -> JsonPrimitive(asObject().toString())
    }
}

internal fun DatabaseObject.toJsonElement(): JsonObject {
    val result = JsonObject()
    names().forEach {
        result.add(it, get(it)?.toJsonElement())
    }
    return result
}

internal fun DatabaseArray.toJsonElement(): JsonArray {
    val result = JsonArray()
    (0 until size()).forEach {
        result.add(get(it)?.toJsonElement())
    }
    return result
}

internal fun DatabasePrimitive.toJsonElement(): JsonElement =
        when {
            isBoolean() -> JsonPrimitive(asBoolean())
            isNumber() -> JsonPrimitive(asNumber())
            isString() -> JsonPrimitive(asString())
            else -> {
                val internalVal = value()
                when (internalVal) {
                    is DatabaseElement -> internalVal.toJsonElement()
                    else -> JsonPrimitive(toString())
                }
            }
        }
