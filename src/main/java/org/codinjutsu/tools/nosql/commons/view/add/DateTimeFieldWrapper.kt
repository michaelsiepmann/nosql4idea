package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive
import org.codinjutsu.tools.nosql.commons.view.table.DateTimePicker
import java.util.*

internal class DateTimeFieldWrapper : TextFieldWrapper<DateTimePicker>(DateTimePicker.create()) {
    init {
        component.editor.isEditable = false
    }

    override fun getValue() = InternalDatabasePrimitive(component.date)

    override val isValueSet: Boolean
        get() = component.date != null

    override fun reset() {
        component.date = GregorianCalendar.getInstance().time
    }
}
