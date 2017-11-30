package org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor

import com.google.gson.JsonObject
import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractNodeDecriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.ValueDescriptor
import java.lang.String.format

internal open class ElasticsearchValueDescriptor(private val index: Int, private var value: Any?, private val valueTextAttributes: SimpleTextAttributes) : AbstractNodeDecriptor(), ValueDescriptor, ElasticsearchDescriptor {

    override fun renderValue(cellRenderer: ColoredTableCellRenderer, isNodeExpanded: Boolean) {
        if (!isNodeExpanded) {
            cellRenderer.append(formattedValue, valueTextAttributes)
        }
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer) {
        cellRenderer.append(formattedKey, StyleAttributesProvider.getIndexAttribute())
    }

    override fun getFormattedKey(): String = format("[%s]", index)

    override fun getFormattedValue(): String = format("%s", getValueAndAbbreviateIfNecessary())

    override fun getValue() = value

    override fun setValue(value: Any?) {
        this.value = value
    }

    override fun buildObject(jsonObject: JsonObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toString() = value.toString()

    private class ElasticsearchStringValueDescriptor(index: Int, value: Any?) : ElasticsearchValueDescriptor(index, value, getStringAttribute()) {

        override fun getFormattedValue(): String = format(""""%s"""", getValueAndAbbreviateIfNecessary())
    }

    companion object {
        fun createDescriptor(index: Int, value: Any) =
                if (value is String) {
                    ElasticsearchStringValueDescriptor(index, value)
                } else {
                    ElasticsearchValueDescriptor(index, value, getStringAttribute())
                }
    }
}