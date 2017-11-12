package org.codinjutsu.tools.nosql.elasticsearch.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class ElasticsearchObjectWrapper(private val jsonObject: JsonObject) : ObjectWrapper {
    override val names: Collection<String>
        get() = getSource()?.entrySet()?.map { it.key } ?: emptyList()

    override fun get(name: String): Any? = getSource()?.get(name)

    override fun isArray(value: Any?) = value is JsonArray

    override fun isObject(value: Any?) = value is JsonObject

    private fun getSource() = jsonObject.get("_source") as JsonObject?
}