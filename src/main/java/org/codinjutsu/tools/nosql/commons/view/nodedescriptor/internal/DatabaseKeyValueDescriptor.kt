package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor

internal class DatabaseKeyValueDescriptor<VALUE>(private val delegate: TypedKeyValueDescriptor<VALUE>) : KeyValueDescriptor<VALUE> by delegate, DatabaseDescriptor {

    override fun buildObject(databaseObject: DatabaseObject) {
        val currentValue = value
        when (currentValue) {
            null -> databaseObject.add(delegate.key, null)
            is Int -> databaseObject.addProperty(delegate.key, currentValue)
            is Boolean -> databaseObject.addProperty(delegate.key, currentValue)
            is Char -> databaseObject.addProperty(delegate.key, currentValue)
            else -> databaseObject.addProperty(formattedKey, currentValue.toString())
        }
    }
}