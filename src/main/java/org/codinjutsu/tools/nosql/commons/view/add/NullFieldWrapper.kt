package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

import javax.swing.JLabel

internal class NullFieldWrapper : TextFieldWrapper<JLabel>(JLabel("null")) {

    override fun getValue(): DatabaseElement? = null

    override fun reset() {}
}
