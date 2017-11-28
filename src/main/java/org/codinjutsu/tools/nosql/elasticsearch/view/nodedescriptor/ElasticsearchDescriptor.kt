package org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor

import com.google.gson.JsonObject

internal interface ElasticsearchDescriptor {
    fun buildObject(jsonObject: JsonObject)
}