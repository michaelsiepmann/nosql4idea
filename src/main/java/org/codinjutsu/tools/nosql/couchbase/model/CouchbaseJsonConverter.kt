package org.codinjutsu.tools.nosql.couchbase.model

import com.couchbase.client.java.document.json.JsonArray
import com.couchbase.client.java.document.json.JsonObject
import com.couchbase.client.java.document.json.JsonValue
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseArrayImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseObjectImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl

internal fun JsonValue.toDatabaseElement(): DatabaseElement {
    return when (this) {
        is JsonObject -> toDatabaseElement()
        is JsonArray -> toDatabaseElement()
        else -> DatabasePrimitiveImpl(toString())
    }
}

internal fun JsonObject.toDatabaseElement(): DatabaseObject {
    val result = DatabaseObjectImpl()
    names.forEach {
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

private fun Any.toDatabaseElement(): DatabaseElement {
    return when (this) {
        is JsonObject -> toDatabaseElement()
        is JsonArray -> toDatabaseElement()
        is JsonValue -> toDatabaseElement()
        else -> DatabasePrimitiveImpl(this)
    }
}

internal fun DatabaseElement.toJsonValue(): Any {
    return when (this) {
        is DatabaseObject -> toJsonValue()
        is DatabaseArray -> toJsonValue()
        is DatabasePrimitive -> toJsonValue()
        else -> asObject().toString()
    }
}

internal fun DatabaseObject.toJsonValue(): JsonObject {
    val result = JsonObject.create()
    names().forEach {
        result.put(it, get(it)?.toJsonValue())
    }
    return result
}

internal fun DatabaseArray.toJsonValue(): JsonArray {
    val result = JsonArray.create()
    (0 until size()).forEach {
        result.add(get(it)?.toJsonValue())
    }
    return result
}

internal fun DatabasePrimitive.toJsonValue(): Any =
        when {
            isBoolean() -> asBoolean()
            isNumber() -> asNumber()
            isString() -> asString()
            else -> {
                val internalVal = value()
                when (internalVal) {
                    is DatabaseElement -> internalVal.toJsonValue()
                    else -> toString()
                }
            }
        }
