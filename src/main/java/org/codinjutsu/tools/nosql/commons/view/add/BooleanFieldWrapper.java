package org.codinjutsu.tools.nosql.commons.view.add;

import com.intellij.ui.components.JBCheckBox;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive;

public class BooleanFieldWrapper extends TextFieldWrapper<JBCheckBox> {

    public BooleanFieldWrapper() {
        super(new JBCheckBox());
    }

    @Override
    public DatabaseElement getValue() {
        return new InternalDatabasePrimitive(component.isSelected());
    }

    @Override
    public void reset() {
        component.setSelected(false);
    }
}
