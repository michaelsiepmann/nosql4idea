package org.codinjutsu.tools.nosql.commons.history

import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.util.xmlb.annotations.Transient
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.configuration.ConfigurationElement
import org.codinjutsu.tools.nosql.commons.model.explorer.TreeCellItem
import org.jdom.Element

internal data class HistoryList @JvmOverloads constructor(
        var vendor: String = DatabaseVendor.MONGO.vendorName,
        var items: PanelList = PanelList()
) : TreeCellItem, ConfigurationElement {

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            val databaseVendor = DatabaseVendor.findByName(vendor)
            append(databaseVendor.vendorName)
            icon = databaseVendor.icon
        }
    }

    fun addItem(historyItem: HistoryItem, project: Project) {
        if (items.none { it.filter == historyItem.filter }) {
            items.add(0, historyItem, vendor, project)
            while (items.unpinnedSize > MAX_SIZE) {
                items.removeFirstUnpinned(vendor, project)
            }
        }
    }

    override fun readElement(element: Element) {
        vendor = element.name
        items = PanelList(
                element
                        .children
                        .map {
                            HistoryItem(it.getAttributeValue("filter"), it.getAttributeValue("pinned", "false")!!.toBoolean())
                        }
                        .toMutableList()
        )
    }

    override fun createElement(): Element {
        val result = Element(vendor)
        items.forEach {
            result.addContent(it.createElement())
        }
        return result
    }

    private fun HistoryItem.createElement(): Element {
        val result = Element("item")
        result.setAttribute("filter", filter)
        result.setAttribute("pinned", pinned.toString())
        return result
    }

    internal data class PanelList(private val delegate: MutableList<HistoryItem> = mutableListOf()) : MutableList<HistoryItem> by delegate {

        val unpinnedSize
            @Transient
            get() = filter { !it.pinned }.size

        fun add(index: Int, element: HistoryItem, vendorName: String, project: Project) {
            delegate.add(index, element)
            sendMessage(project) {
                it.add(vendorName, index, element)
            }
        }

        fun removeFirstUnpinned(vendorName: String, project: Project) {
            removeAt(indexOfFirst { !it.pinned }, vendorName, project)
        }

        private fun removeAt(index: Int, vendorName: String, project: Project) {
            delegate.removeAt(index)
            sendMessage(project) {
                it.removeAt(vendorName, index)
            }
        }

        private fun sendMessage(project: Project, topic: (HistoryPanelMessages) -> Unit) {
            topic(project.messageBus.syncPublisher(HistoryPanelMessages.TOPIC))
        }
    }

    companion object {
        private const val MAX_SIZE = 100
    }
}