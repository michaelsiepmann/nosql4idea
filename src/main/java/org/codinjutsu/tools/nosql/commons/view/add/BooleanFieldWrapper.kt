package org.codinjutsu.tools.nosql.commons.view.add

import com.intellij.ui.components.JBCheckBox
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl

internal class BooleanFieldWrapper : AbstractTextFieldWrapper<JBCheckBox>(JBCheckBox()) {

    override fun getValue() = DatabasePrimitiveImpl(component.isSelected)

    override fun reset() {
        component.isSelected = false
    }
}
