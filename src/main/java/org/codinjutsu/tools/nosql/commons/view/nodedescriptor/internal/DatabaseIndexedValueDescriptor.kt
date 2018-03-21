package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getObjectAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractNodeDecriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor

internal open class DatabaseIndexedValueDescriptor(
        private val index: Int,
        private var value: Any?,
        private val valueTextAttributes: SimpleTextAttributes
) : AbstractNodeDecriptor<Any?>(), IndexedValueDescriptor<Any?> {

    override fun renderValue(cellRenderer: ColoredTableCellRenderer, isNodeExpanded: Boolean) {
        if (!isNodeExpanded) {
            cellRenderer.append(formattedValue, valueTextAttributes)
        }
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer) {
        cellRenderer.append(formattedKey, StyleAttributesProvider.getIndexAttribute())
    }

    override fun getFormattedKey(): String = "[$index]"

    override fun getFormattedValue(): String = getValueAndAbbreviateIfNecessary()

    override fun getValue() = value

    override fun setValue(value: Any?) {
        this.value = value
    }

    override fun toString() = value.toString()

    private class DatabaseStringIndexedValueDescriptor(index: Int, value: Any?) : DatabaseIndexedValueDescriptor(index, value, getStringAttribute()) {

        override fun getFormattedValue(): String = """"${getValueAndAbbreviateIfNecessary()}""""
    }

    companion object {
        fun createDescriptor(index: Int, value: Any?) =
                if (value is String) {
                    DatabaseStringIndexedValueDescriptor(index, value)
                } else {
                    DatabaseIndexedValueDescriptor(index, value, getObjectAttribute())
                }
    }
}