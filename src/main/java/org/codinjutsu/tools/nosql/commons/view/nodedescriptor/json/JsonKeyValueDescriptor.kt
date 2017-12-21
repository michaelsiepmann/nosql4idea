package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getBooleanAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getNumberAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor
import java.lang.String.format

internal open class JsonKeyValueDescriptor(key: String, _value: Any?, valueTextAttributes: SimpleTextAttributes) :
        AbstractKeyValueDescriptor(key, _value, valueTextAttributes), JsonDescriptor {

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

    private class JsonKeyBooleanValueDecriptor(key: String, value: Boolean) : JsonKeyValueDescriptor(key, value, getBooleanAttribute())

    private class JsonKeyIntegerValueDescriptor(key: String, value: Number) : JsonKeyValueDescriptor(key, value, getNumberAttribute())

    private class JsonKeyStringValueDescriptor(key: String, value: String) : JsonKeyValueDescriptor(key, value, getStringAttribute()) {

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
                value.isBoolean -> JsonKeyBooleanValueDecriptor(key, value.asBoolean)
                value.isNumber -> JsonKeyIntegerValueDescriptor(key, value.asNumber)
                value.isString -> JsonKeyStringValueDescriptor(key, value.asString)
                else -> JsonKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
            }
            is String -> JsonKeyStringValueDescriptor(key, value)
            else -> JsonKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
        }
    }
}