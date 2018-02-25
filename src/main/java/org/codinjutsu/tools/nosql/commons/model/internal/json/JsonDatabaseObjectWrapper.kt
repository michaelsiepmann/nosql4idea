package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class JsonDatabaseObjectWrapper(private val delegate: ObjectWrapper) : ObjectWrapper by delegate {

    override fun get(name: String): Any? {
        val element = delegate.get(name)
        return when (element) {
            is JsonObject -> JsonDatabaseObject(element)
            is JsonArray -> JsonDatabaseArray(element)
            is JsonPrimitive -> JsonDatabasePrimitive(element)
            else -> element
        }
    }
}