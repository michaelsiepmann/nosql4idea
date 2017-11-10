package org.codinjutsu.tools.nosql.commons.utils

import com.couchbase.client.java.document.json.JsonArray
import com.couchbase.client.java.document.json.JsonObject
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider

fun getSimpleTextAttributes(value: Any) = when (value) {
    is Boolean -> StyleAttributesProvider.getBooleanAttribute()
    is Number -> StyleAttributesProvider.getNumberAttribute()
    is JsonObject -> StyleAttributesProvider.getObjectAttribute()
    is JsonArray -> StyleAttributesProvider.getObjectAttribute()
    else -> StyleAttributesProvider.getStringAttribute()
}
