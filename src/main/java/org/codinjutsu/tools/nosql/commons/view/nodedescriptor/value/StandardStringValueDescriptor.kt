package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider

internal class StandardStringValueDescriptor internal constructor(index: Int, value: String) : StandardValueDescriptor<String>(index, value, StyleAttributesProvider.getStringAttribute()) {

    override fun getFormattedValue() = "\"${getValueAndAbbreviateIfNecessary()}\""
}