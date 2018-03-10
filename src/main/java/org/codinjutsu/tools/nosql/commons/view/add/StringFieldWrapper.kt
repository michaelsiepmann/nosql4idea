package org.codinjutsu.tools.nosql.commons.view.add

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive

internal class StringFieldWrapper : JTextFieldWrapper() {

    override fun getValue() = InternalDatabasePrimitive(component.text)
}
