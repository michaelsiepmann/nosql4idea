package org.codinjutsu.tools.nosql.commons.view.add

import javax.swing.JComponent

internal abstract class AbstractTextFieldWrapper<out T : JComponent> internal constructor(override val component: T) : TextFieldWrapper<T> {

    override val isValueSet: Boolean
        get() = true

    override fun validate() {
        if (!isValueSet) {
            throw IllegalArgumentException("Value is not set")
        }
    }
}
