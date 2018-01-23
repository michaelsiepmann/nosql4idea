package org.codinjutsu.tools.nosql.commons.model

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class JsonObjectObjectWrapper(private val jsonObject: JsonObject) : ObjectWrapper {
    override val names: Collection<String>
        get() = jsonObject.entrySet().map { it.key }

    override fun get(name: String): Any? = jsonObject.get(name)
}