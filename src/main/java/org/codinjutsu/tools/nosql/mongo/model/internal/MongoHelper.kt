package org.codinjutsu.tools.nosql.mongo.model.internal

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
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

private fun convertUnknown(value: Any): DatabaseElement? {
    if (value is DBObject) {
        return convert(value)
    } else {
        return InternalDatabasePrimitive(value.toString())
    }
}

private fun convertToInternalPrimitive(dbObject: DBObject): DatabaseElement {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
