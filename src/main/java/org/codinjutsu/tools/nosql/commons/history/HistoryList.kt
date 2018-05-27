package org.codinjutsu.tools.nosql.commons.history

import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.model.explorer.TreeCellItem

internal class HistoryList(val vendor: String) : TreeCellItem {

    val items: PanelList = PanelList(mutableListOf())

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            val databaseVendor = DatabaseVendor.valueOf(vendor)
            append(databaseVendor.vendorName)
            icon = databaseVendor.icon
        }
    }

    fun addItem(filter: String, project: Project) {
        if (items.none { it.filter == filter }) {
            items.add(0, HistoryItem(filter), vendor, project)
            while (items.unpinnedSize > MAX_SIZE) {
                items.removeFirstUnpinned(vendor, project)
            }
        }
    }

    internal class PanelList(private val delegate: MutableList<HistoryItem>) : MutableList<HistoryItem> by delegate {

        val unpinnedSize
            get() = filter { !it.pinned }.size

        fun add(index: Int, element: HistoryItem, vendorName: String, project: Project) {
            delegate.add(index, element)
            sendMessage(project) {
                it.add(vendorName, index, element)
            }
        }

        fun removeFirstUnpinned(vendorName : String, project: Project) {
            removeAt(indexOfFirst { !it.pinned }, vendorName, project)
        }

        private fun removeAt(index: Int, vendorName : String, project: Project) {
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