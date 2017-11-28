package org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getBooleanAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getNumberAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor
import java.lang.String.format

internal open class ElasticsearchKeyValueDescriptor(key: String, _value: Any?, valueTextAttributes: SimpleTextAttributes) :
        AbstractKeyValueDescriptor(key, _value, valueTextAttributes), ElasticsearchDescriptor {

    override fun buildObject(jsonObject: JsonObject) {
        val currentValue = value
        when (currentValue) {
            null -> jsonObject.add(key, null)
            is Int -> jsonObject.addProperty(key, currentValue)
            is Boolean -> jsonObject.addProperty(key, currentValue)
            is Char -> jsonObject.addProperty(key, currentValue)
            else -> jsonObject.addProperty(formattedKey, currentValue.toString())
        }
    }

    private class ElasticsearchKeyBooleanValueDecriptor(key: String, value: Boolean) : ElasticsearchKeyValueDescriptor(key, value, getBooleanAttribute())

    private class ElasticsearchKeyIntegerValueDescriptor(key:String, value: Number) : ElasticsearchKeyValueDescriptor(key, value, getNumberAttribute())

    private class ElasticsearchKeyStringValueDescriptor(key: String, value: String) : ElasticsearchKeyValueDescriptor(key, value, getStringAttribute()) {

        override fun getValueAndAbbreviateIfNecessary(): String {
            return format(""""%s"""", value)
        }

        override fun toString(): String {
            return format(""""%s" : "%s"""", key, value)
        }
    }

    companion object {
        fun createDescriptor(key: String, value: Any?) = when (value) {
            null -> DefaultKeyNullValueDescriptor(key)
            is JsonPrimitive -> when {
                value.isBoolean -> ElasticsearchKeyBooleanValueDecriptor(key, value.asBoolean)
                value.isNumber -> ElasticsearchKeyIntegerValueDescriptor(key, value.asNumber)
                value.isString -> ElasticsearchKeyStringValueDescriptor(key, value.asString)
                else -> ElasticsearchKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
            }
            is String -> ElasticsearchKeyStringValueDescriptor(key, value)
            else -> ElasticsearchKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
        }
    }
}