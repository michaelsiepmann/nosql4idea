package org.codinjutsu.tools.nosql.mongo.model.internal

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabaseArray
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabaseObject
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive

internal fun convert(dbObject: DBObject?): DatabaseElement? {
    return when (dbObject) {
        null -> null
        is BasicDBList -> convertToInternalArray(dbObject)
        is BasicDBObject -> convertToInternalObject(dbObject)
        else -> convertToInternalPrimitive(dbObject)
    }
}

private fun convertToInternalArray(dbObject: BasicDBList): DatabaseElement {
    val result = InternalDatabaseArray()
    (0 until dbObject.size).forEach { i -> result.add(convertUnknown(dbObject[i])!!) }
    return result
}

private fun convertToInternalObject(dbObject: BasicDBObject): DatabaseElement {
    val result = InternalDatabaseObject()
    dbObject.keys.forEach {
        result.add(it, convertUnknown(dbObject.get(it)))
    }
    return result
}

private fun convertUnknown(value: Any) =
        if (value is DBObject) {
            convert(value)
        } else {
            InternalDatabasePrimitive(value)
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
        result.append(it, revertInternal(element.get(it)))
    }
    return result
}

private fun revertArray(element: DatabaseArray): DBObject? {
    val result = BasicDBList()
    element.toArray().forEach {
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
