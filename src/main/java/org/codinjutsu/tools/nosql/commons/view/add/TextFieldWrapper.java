package org.codinjutsu.tools.nosql.commons.view.add;

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;

import javax.swing.JComponent;

public abstract class TextFieldWrapper<T extends JComponent> {

    protected final T component;

    TextFieldWrapper(T component) {
        this.component = component;
    }

    public abstract DatabaseElement getValue();

    public abstract void reset();

    public boolean isValueSet() {
        return true;
    }

    public JComponent getComponent() {
        return component;
    }

    public void validate() {
        if (!isValueSet()) {
            throw new IllegalArgumentException("Value is not set");
        }
    }
}
