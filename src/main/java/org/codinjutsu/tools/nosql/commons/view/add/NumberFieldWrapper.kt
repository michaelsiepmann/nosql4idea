package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.utils.parseNumber
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive

internal class NumberFieldWrapper : JTextFieldWrapper() {

    override fun getValue() = InternalDatabasePrimitive(parseValue())

    private fun parseValue() = component.text.parseNumber()

    override fun validate() {
        super.validate()
        parseValue()
    }
}
