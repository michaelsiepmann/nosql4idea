package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.utils.abbreviateInCenter

abstract class AbstractNodeDecriptor<out VALUE> {

    protected abstract fun getValue() : VALUE

    protected open fun getValueAndAbbreviateIfNecessary() =
            getValueAndAbbreviateIfNecessary(getValue().toString())

    protected fun getValueAndAbbreviateIfNecessary(stringifiedValue: String?) =
            when {
                stringifiedValue == null -> ""
                stringifiedValue.length > NodeDescriptor.MAX_LENGTH -> stringifiedValue.abbreviateInCenter(NodeDescriptor.MAX_LENGTH)
                else -> stringifiedValue
            }
}