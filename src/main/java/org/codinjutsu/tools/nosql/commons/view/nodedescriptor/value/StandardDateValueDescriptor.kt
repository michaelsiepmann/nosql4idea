package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.utils.DateUtils
import java.util.*

internal class StandardDateValueDescriptor internal constructor(index: Int, value: Date) : StandardValueDescriptor<Date>(index, value, StyleAttributesProvider.getStringAttribute()) {

    private val formattedDate: String
        get() = DATE_FORMAT.format(value)

    override fun getValueAndAbbreviateIfNecessary() = formattedDate

    override fun toString() = "\"$formattedDate\""

    companion object {
        private val DATE_FORMAT = DateUtils.utcDateTime(Locale.getDefault())
    }
}