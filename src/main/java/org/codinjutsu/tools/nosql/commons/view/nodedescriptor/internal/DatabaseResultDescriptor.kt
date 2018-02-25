package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor

internal class DatabaseResultDescriptor(collectionName: String) : ResultDescriptor<Any?> {

    private val formattedText = "results of '$collectionName'"

    override fun renderValue(cellRenderer: ColoredTableCellRenderer?, isNodeExpanded: Boolean) {
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer?) {
    }

    override fun getFormattedKey() = formattedText

    override fun getFormattedValue() = ""

    override fun getValue(): Any? = null

    override fun setValue(value: Any?) {
    }
}