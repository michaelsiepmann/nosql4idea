package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.google.gson.JsonArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

internal class JsonDatabaseArray(internal val jsonArray: JsonArray) : DatabaseArray {
    override fun size() = jsonArray.size()

    override fun get(index: Int) = convert(jsonArray[index])

    override fun toString() = jsonArray.toString()

    override fun toArray(): Iterator<DatabaseElement> {
        val result = mutableListOf<DatabaseElement>()
        jsonArray.iterator().forEach {
            convert(it)?.let {
                result.add(it)
            }
        }
        return result.iterator()
    }
}