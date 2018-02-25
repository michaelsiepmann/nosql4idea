package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractNodeDecriptor
import javax.swing.Icon

open class TypedKeyValueDescriptor<VALUE> @JvmOverloads constructor(
        override val key: String,
        private var _value: VALUE,
        private val valueTextAttributes: SimpleTextAttributes,
        private val icon: Icon? = null
) : AbstractNodeDecriptor<VALUE>(), KeyValueDescriptor<VALUE> {

    override fun renderValue(cellRenderer: ColoredTableCellRenderer, isNodeExpanded: Boolean) {
        if (!isNodeExpanded) {
            cellRenderer.append(getValueAndAbbreviateIfNecessary(), valueTextAttributes)
        }
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer) {
        cellRenderer.append(formattedKey, StyleAttributesProvider.getKeyValueAttribute())
        cellRenderer.icon = icon
    }

    override fun getFormattedKey() = key

    override fun getFormattedValue() = getValueAndAbbreviateIfNecessary()

    override fun getValue() = _value

    override fun setValue(value: Any?) {
        _value = value as VALUE
    }

    override fun isSameKey(key: String?) = key == this.key

    override fun toString() = "\"$key\" : $value"
}