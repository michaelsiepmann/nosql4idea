package org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor

import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor
import java.lang.String.format

internal open class ElasticsearchKeyValueDescriptor(key: String, _value: Any?, valueTextAttributes: SimpleTextAttributes) :
        AbstractKeyValueDescriptor(key, _value, valueTextAttributes) {

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
            is String -> ElasticsearchKeyStringValueDescriptor(key, value)
            else -> ElasticsearchKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
        }
    }
}