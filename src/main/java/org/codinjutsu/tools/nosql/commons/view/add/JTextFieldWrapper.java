package org.codinjutsu.tools.nosql.commons.view.add;

import javax.swing.JTextField;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public abstract class JTextFieldWrapper extends TextFieldWrapper<JTextField> {

    JTextFieldWrapper() {
        super(new JTextField());
    }

    @Override
    public boolean isValueSet() {
        return isNotBlank(component.getText());
    }

    @Override
    public void reset() {
        component.setText("");
    }
}
