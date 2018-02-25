package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider

internal class StandardNullIndexedValueDescriptor internal constructor(index: Int) : StandardIndexedValueDescriptor<Any?>(index, null, StyleAttributesProvider.getNullAttribute()) {

    override fun getFormattedValue() = "null"

    override fun toString() = "null"
}