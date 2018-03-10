package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

import javax.swing.JComponent

internal abstract class TextFieldWrapper<out T : JComponent> internal constructor(val component: T) {

    abstract fun getValue(): DatabaseElement?

    open val isValueSet: Boolean
        get() = true

    abstract fun reset()

    open fun validate() {
        if (!isValueSet) {
            throw IllegalArgumentException("Value is not set")
        }
    }
}
