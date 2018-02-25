package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal class JsonDatabaseObject(internal val jsonObject: JsonObject) : DatabaseObject {
    override fun asObject() = this

    override fun names(): Collection<String> = jsonObject.keySet()

    override fun get(key: String) = convert(jsonObject.get(key))

    override fun toString() = jsonObject.toString()
}
