package org.codinjutsu.tools.nosql.commons.view.add;

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;

import javax.swing.JLabel;

public class NullFieldWrapper extends TextFieldWrapper<JLabel> {

    public NullFieldWrapper() {
        super(new JLabel("null"));
    }

    @Override
    public DatabaseElement getValue() {
        return null;
    }

    @Override
    public void reset() {
    }
}
