package org.codinjutsu.tools.nosql.commons.view.add;

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive;

public class StringFieldWrapper extends JTextFieldWrapper {

    @Override
    public DatabaseElement getValue() {
        return new InternalDatabasePrimitive(component.getText());
    }
}
