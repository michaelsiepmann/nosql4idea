package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import org.apache.commons.lang.StringUtils
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import java.lang.String.format

internal abstract class AbstractKeyValueDescriptor(val key: String, private var _value: Any?, private val valueTextAttributes: SimpleTextAttributes) : AbstractNodeDecriptor() {

    override fun renderValue(cellRenderer: ColoredTableCellRenderer, isNodeExpanded: Boolean) {
        if (!isNodeExpanded) {
            cellRenderer.append(getValueAndAbbreviateIfNecessary(), valueTextAttributes)
        }
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer) {
        cellRenderer.append(formattedKey, StyleAttributesProvider.getKeyValueAttribute())
    }

    override fun getFormattedKey() = key

    override fun getFormattedValue() = getValueAndAbbreviateIfNecessary()

    override fun getValue() = _value

    override fun setValue(value: Any?) {
        _value = value
    }

    override fun isSameKey(key: String?) = StringUtils.equals(key, this.key)

    override fun toString() = format(TO_STRING_TEMPLATE, key, value)

    protected open class DefaultKeyNullValueDescriptor(key: String) : AbstractKeyValueDescriptor(key, null, StyleAttributesProvider.getNullAttribute()) {
        override fun getValueAndAbbreviateIfNecessary() = "null"
    }

    companion object {
        private const val TO_STRING_TEMPLATE = "\"%s\" : %s"

    }
}