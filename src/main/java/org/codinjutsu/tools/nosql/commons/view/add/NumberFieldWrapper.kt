package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl
import org.codinjutsu.tools.nosql.commons.utils.parseNumber

internal class NumberFieldWrapper : JTextFieldWrapper() {

    override fun getValue() = DatabasePrimitiveImpl(parseValue())

    private fun parseValue() = component.text.parseNumber()

    override fun validate() {
        super.validate()
        parseValue()
    }
}
