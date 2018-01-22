package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.utils.DateUtils
import java.util.*

class DateKeyValueDescriptor internal constructor(key: String, value: Date) : TypedKeyValueDescriptor<Date>(key, value, getStringAttribute()) {

    private val formattedDate: String
        get() = DATE_FORMAT.format(value)

    override fun getValueAndAbbreviateIfNecessary() = formattedDate

    override fun toString() = "\"$value\" : \"$formattedDate\""

    companion object {
        private val DATE_FORMAT = DateUtils.utcDateTime(Locale.getDefault())
    }
}