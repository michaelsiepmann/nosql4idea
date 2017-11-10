package org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractNodeDecriptor
import java.lang.String.format

internal open class ElasticsearchValueDescriptor(private val index: Int, private var value: Any?, private val valueTextAttributes: SimpleTextAttributes) : AbstractNodeDecriptor() {

    override fun renderValue(cellRenderer: ColoredTableCellRenderer, isNodeExpanded: Boolean) {
        if (!isNodeExpanded) {
            cellRenderer.append(formattedValue, valueTextAttributes)
        }
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer) {
        cellRenderer.append(formattedKey, StyleAttributesProvider.getIndexAttribute())
    }

    override fun getFormattedKey() = format("[%s]", index)

    override fun getFormattedValue() = format("%s", getValueAndAbbreviateIfNecessary())

    override fun getValue() = value

    override fun setValue(value: Any?) {
        this.value = value
    }

    override fun toString() = value.toString()

    private class ElasticsearchStringValueDescriptor(index: Int, value: Any?) : ElasticsearchValueDescriptor(index, value, StyleAttributesProvider.getStringAttribute()) {

        override fun getFormattedValue() = format(""""%s"""", getValueAndAbbreviateIfNecessary())
    }

    companion object {
        fun createDescriptor(index: Int, value: Any) =
                if (value is String) {
                    ElasticsearchStringValueDescriptor(index, value)
                } else {
                    ElasticsearchValueDescriptor(index, value, StyleAttributesProvider.getStringAttribute())
                }
    }
}