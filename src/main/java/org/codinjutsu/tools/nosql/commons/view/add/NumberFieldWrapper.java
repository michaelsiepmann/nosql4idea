package org.codinjutsu.tools.nosql.commons.view.add;

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive;
import org.jetbrains.annotations.NotNull;

import static org.codinjutsu.tools.nosql.commons.utils.StringUtilsKt.parseNumber;

public class NumberFieldWrapper extends JTextFieldWrapper {

    @Override
    public DatabaseElement getValue() {
        return new InternalDatabasePrimitive(parseValue());
    }

    @NotNull
    private Number parseValue() {
        return parseNumber(component.getText());
    }

    @Override
    public void validate() {
        super.validate();
        parseValue();
    }
}
