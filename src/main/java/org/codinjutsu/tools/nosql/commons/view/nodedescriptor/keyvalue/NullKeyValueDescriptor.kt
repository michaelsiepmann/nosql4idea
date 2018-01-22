package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getNullAttribute

class NullKeyValueDescriptor(key : String) : TypedKeyValueDescriptor<Any?>(key, null, getNullAttribute()) {
    override fun getValueAndAbbreviateIfNecessary() = "null"
}