package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute

class StringKeyValueDescriptor(key: String, _value: String) :
        TypedKeyValueDescriptor<String>(key, _value, getStringAttribute()) {
    override fun getValueAndAbbreviateIfNecessary(): String {
        return java.lang.String.format(""""%s"""", value)
    }
}