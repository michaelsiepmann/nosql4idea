package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor

class JsonKeyValueDescriptor<VALUE>(private val delegate: TypedKeyValueDescriptor<VALUE>) : KeyValueDescriptor<VALUE> by delegate, JsonDescriptor {

    override fun buildObject(jsonObject: JsonObject) {
        val currentValue = value
        when (currentValue) {
            null -> jsonObject.add(delegate.key, null)
            is Int -> jsonObject.addProperty(delegate.key, currentValue)
            is Boolean -> jsonObject.addProperty(delegate.key, currentValue)
            is Char -> jsonObject.addProperty(delegate.key, currentValue)
            else -> jsonObject.addProperty(formattedKey, currentValue.toString())
        }
    }
}