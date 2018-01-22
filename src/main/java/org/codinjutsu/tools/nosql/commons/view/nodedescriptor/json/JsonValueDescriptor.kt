package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json

import com.google.gson.JsonObject
import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractNodeDecriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.ValueDescriptor
import java.lang.String.format

internal open class JsonValueDescriptor(
        private val index: Int,
        private var value: Any?,
        private val valueTextAttributes: SimpleTextAttributes
) : AbstractNodeDecriptor<Any?>(), ValueDescriptor<Any?>, JsonDescriptor {

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

    private class JsonStringValueDescriptor(index: Int, value: Any?) : JsonValueDescriptor(index, value, getStringAttribute()) {

        override fun getFormattedValue(): String = format(""""%s"""", getValueAndAbbreviateIfNecessary())
    }

    companion object {
        fun createDescriptor(index: Int, value: Any) =
                if (value is String) {
                    JsonStringValueDescriptor(index, value)
                } else {
                    JsonValueDescriptor(index, value, getStringAttribute())
                }
    }
}