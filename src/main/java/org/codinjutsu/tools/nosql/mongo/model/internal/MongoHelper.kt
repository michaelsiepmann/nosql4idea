package org.codinjutsu.tools.nosql.mongo.model.internal

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseArrayImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseObjectImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl

internal fun DBObject.toDatabaseElement(): DatabaseElement {
    return when (this) {
        is BasicDBList -> convertToInternalArray()
        is BasicDBObject -> convertToInternalObject()
        else -> convertToInternalPrimitive(this)
    }
}

internal fun DBObject.toDatabaseObject(): DatabaseObject {
    return if (this is BasicDBObject) {
        convertToInternalObject()
    } else {
        toDatabaseElement() as DatabaseObject
    }
}

private fun BasicDBList.convertToInternalArray(): DatabaseElement {
    val result = DatabaseArrayImpl()
    (0 until size).forEach { i -> result.add(convertUnknown(get(i))) }
    return result
}

private fun BasicDBObject.convertToInternalObject(): DatabaseObject {
    val result = DatabaseObjectImpl()
    keys.forEach {
        result.add(it, convertUnknown(get(it)))
    }
    return result
}

private fun convertUnknown(value: Any) =
        if (value is DBObject) {
            value.toDatabaseElement()
        } else {
            DatabasePrimitiveImpl(value)
        }

private fun convertToInternalPrimitive(dbObject: DBObject): DatabaseElement {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

internal fun revert(element: DatabaseElement?): DBObject? {
    return revertInternal(element) as DBObject?
}

private fun revertInternal(element: DatabaseElement?): Any? {
    return when (element) {
        null -> null
        is DatabaseObject -> revertObject(element)
        is DatabaseArray -> revertArray(element)
        else -> revertPrimitive(element as DatabasePrimitive)
    }
}

private fun revertObject(element: DatabaseObject): DBObject? {
    val result = BasicDBObject()
    element.names().forEach {
        val revertInternal = revertInternal(element[it])
        if (revertInternal != null) {
            result.append(it, revertInternal)
        }
    }
    return result
}

private fun revertArray(element: DatabaseArray): DBObject? {
    val result = BasicDBList()
    element.forEach {
        result.add(revertInternal(it))
    }
    return result
}

private fun revertPrimitive(element: DatabasePrimitive): Any? {
    val value = element.value()
    return when {
        element.isBoolean() -> element.asBoolean()
        element.isNumber() -> element.asNumber()
        element.isString() -> element.asString()
        value is DatabaseElement -> revertInternal(value)
        value is ObjectId -> value
        else -> element.toString()
    }
}
