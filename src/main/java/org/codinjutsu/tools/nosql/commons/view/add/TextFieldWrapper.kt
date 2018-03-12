package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import javax.swing.JComponent

internal interface TextFieldWrapper<out T : JComponent> {
    val component: T
    val isValueSet: Boolean

    fun getValue(): DatabaseElement?
    fun reset()
    fun validate()
}