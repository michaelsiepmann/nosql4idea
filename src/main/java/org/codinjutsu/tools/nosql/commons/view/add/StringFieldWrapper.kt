package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl

internal class StringFieldWrapper : JTextFieldWrapper() {

    override fun getValue() = DatabasePrimitiveImpl(component.text)
}
