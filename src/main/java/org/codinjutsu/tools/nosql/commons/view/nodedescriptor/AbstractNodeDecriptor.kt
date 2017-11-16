package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.utils.StringUtils

internal abstract class AbstractNodeDecriptor : NodeDescriptor {

    protected open fun getValueAndAbbreviateIfNecessary() =
            getValueAndAbbreviateIfNecessary(value.toString())

    protected fun getValueAndAbbreviateIfNecessary(stringifiedValue: String) =
            if (stringifiedValue.length > NodeDescriptor.MAX_LENGTH) {
                StringUtils.abbreviateInCenter(stringifiedValue, NodeDescriptor.MAX_LENGTH)
            } else {
                stringifiedValue
            }
}