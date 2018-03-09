package org.codinjutsu.tools.nosql.commons.view.add;

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive;
import org.codinjutsu.tools.nosql.commons.view.table.DateTimePicker;

import java.util.GregorianCalendar;

public class DateTimeFieldWrapper extends TextFieldWrapper<DateTimePicker> {

    public DateTimeFieldWrapper() {
        super(DateTimePicker.create());
        component.getEditor().setEditable(false);
    }

    @Override
    public DatabaseElement getValue() {
        return new InternalDatabasePrimitive(component.getDate());
    }

    @Override
    public boolean isValueSet() {
        return component.getDate() != null;
    }

    @Override
    public void reset() {
        component.setDate(GregorianCalendar.getInstance().getTime());
    }
}
