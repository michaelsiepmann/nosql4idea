package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider

internal class StandardStringIndexedValueDescriptor internal constructor(index: Int, value: String) : StandardIndexedValueDescriptor<String>(index, value, StyleAttributesProvider.getStringAttribute()) {

    override fun getFormattedValue() = "\"${getValueAndAbbreviateIfNecessary()}\""
}