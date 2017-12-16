package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.utils.abbreviateInCenter

internal abstract class AbstractNodeDecriptor : NodeDescriptor {

    protected open fun getValueAndAbbreviateIfNecessary() =
            getValueAndAbbreviateIfNecessary(value.toString())

    protected fun getValueAndAbbreviateIfNecessary(stringifiedValue: String?) =
            when {
                stringifiedValue == null -> ""
                stringifiedValue.length > NodeDescriptor.MAX_LENGTH -> stringifiedValue.abbreviateInCenter(NodeDescriptor.MAX_LENGTH)
                else -> stringifiedValue
            }
}