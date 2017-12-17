package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json

import com.google.gson.JsonObject

internal interface JsonDescriptor {
    fun buildObject(jsonObject: JsonObject)
}