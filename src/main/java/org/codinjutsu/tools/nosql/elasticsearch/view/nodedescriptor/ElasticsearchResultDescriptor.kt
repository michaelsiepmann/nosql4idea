package org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.ResultDescriptor

internal class ElasticsearchResultDescriptor(collectionName : String) : NodeDescriptor, ResultDescriptor {

    private val formattedText = String.format("results of '%s'", collectionName)

    override fun renderValue(cellRenderer: ColoredTableCellRenderer?, isNodeExpanded: Boolean) {
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer?) {
    }

    override fun getFormattedKey() = formattedText

    override fun getFormattedValue() = ""

    override fun getValue() = null

    override fun setValue(value: Any?) {
    }
}