package org.codinjutsu.tools.nosql.commons.view.add

import javax.swing.JTextField

internal abstract class JTextFieldWrapper internal constructor() : AbstractTextFieldWrapper<JTextField>(JTextField()) {

    override val isValueSet: Boolean
        get() = component.text.isNotBlank()

    override fun reset() {
        component.text = ""
    }
}
