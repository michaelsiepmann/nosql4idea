package org.codinjutsu.tools.nosql.commons.view.explorer

import com.intellij.ui.treeStructure.Tree
import org.codinjutsu.tools.nosql.NoSqlTreeRenderer
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeSelectionModel

internal class ExplorerTree internal constructor() : Tree() {

    private val myLabel = JLabel(String.format("<html><center>NoSql server list is empty<br><br>You may use <img src=\"%s\"> to add configuration</center></html>", pluginSettingsUrl))

    init {
        emptyText.clear()
        getSelectionModel().selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        setCellRenderer(NoSqlTreeRenderer())
        name = "databaseTree" //NON-NLS
    }

    val rootNode
        get() = model.root as DefaultMutableTreeNode?

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val model = model
        if (model != null && model.getChildCount(model.root) != 0) {
            return
        }

        myLabel.font = font
        myLabel.background = background
        myLabel.foreground = foreground
        val bounds = bounds
        val size = myLabel.preferredSize
        myLabel.setBounds(0, 0, size.width, size.height)

        val x = (bounds.width - size.width) / 2
        val g2 = g.create(bounds.x + x, bounds.y + 20, bounds.width, bounds.height)
        try {
            myLabel.paint(g2)
        } finally {
            g2.dispose()
        }
    }

    fun getParentOf(folder: Folder<*, *>): DefaultMutableTreeNode? {
        return getParentOf(model.root as DefaultMutableTreeNode, folder)
    }

    private fun getParentOf(node: DefaultMutableTreeNode?, folder: Folder<*, *>): DefaultMutableTreeNode? {
        if (node != null) {
            val children = node.children()
            while (children.hasMoreElements()) {
                val childNode = children.nextElement() as DefaultMutableTreeNode
                if (childNode.userObject === folder) {
                    return node
                }
                val result = getParentOf(childNode, folder)
                if (result != null) {
                    return result
                }
            }
        }
        return null
    }

    fun addDoubleClickListener(doubleClickEvent: (DefaultMutableTreeNode) -> Unit) {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(mouseEvent: MouseEvent) {
                if (mouseEvent.source !is JTree) {
                    return
                }
                if (mouseEvent.clickCount == 2) {
                    doubleClickEvent(lastSelectedPathComponent as DefaultMutableTreeNode)
                }
            }
        })
    }

    companion object {
        private val pluginSettingsUrl = if (GuiUtils.isUnderDarcula()) {
            GuiUtils.getIconResource("pluginSettings_dark.png")
        } else {
            GuiUtils.getIconResource("pluginSettings.png")
        }
    }

}
