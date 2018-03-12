package org.codinjutsu.tools.nosql.commons.view.add

import com.intellij.ui.components.JBCheckBox
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive

internal class BooleanFieldWrapper : AbstractTextFieldWrapper<JBCheckBox>(JBCheckBox()) {

    override fun getValue() = InternalDatabasePrimitive(component.isSelected)

    override fun reset() {
        component.isSelected = false
    }
}
