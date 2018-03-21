package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl
import org.codinjutsu.tools.nosql.commons.view.table.DateTimePicker
import java.util.*

internal class DateTimeFieldWrapper : AbstractTextFieldWrapper<DateTimePicker>(DateTimePicker.create()) {
    init {
        component.editor.isEditable = false
    }

    override fun getValue() = DatabasePrimitiveImpl(component.date)

    override val isValueSet: Boolean
        get() = component.date != null

    override fun reset() {
        component.date = GregorianCalendar.getInstance().time
    }
}
