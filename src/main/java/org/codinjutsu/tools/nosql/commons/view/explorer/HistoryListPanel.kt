package org.codinjutsu.tools.nosql.commons.view.explorer

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.NoSqlConfiguration
import org.codinjutsu.tools.nosql.commons.history.CreateHistoryMessage
import org.codinjutsu.tools.nosql.commons.history.HistoryItem
import org.codinjutsu.tools.nosql.commons.history.HistoryList
import org.codinjutsu.tools.nosql.commons.history.HistoryPanelMessages
import org.codinjutsu.tools.nosql.commons.history.HistorySelectedMessage
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.action.PinHistoryItemAction
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

internal class HistoryListPanel(private val project: Project) : AbstractListPanel(), CreateHistoryMessage, HistoryPanelMessages {

    override fun installActions(databaseTree: ExplorerTree) {
        val expandCollapseButtons = createExpandCollapseButtons()
        val expandAllAction = expandCollapseButtons.component1()
        val collapseAllAction = expandCollapseButtons.component2()
        val actionGroup = DefaultActionGroup("NoSqlHistoryGroup", false) //NON-NLS
        if (ApplicationManager.getApplication() != null) {
            actionGroup.add(expandAllAction)
            actionGroup.add(collapseAllAction)
            actionGroup.add(PinHistoryItemAction(this))
        }
        GuiUtils.installActionGroupInToolBar(actionGroup, toolBarPanel, ActionManager.getInstance(), "NoSqlHistoryActions", true) //NON-NLS

        val messageBus = project.messageBus
        databaseTree.addDoubleClickListener {
            val obj = it.userObject
            if (obj is HistoryItem) {
                val historyList = historyList(it)
                if (historyList != null) {
                    messageBus.syncPublisher(HistorySelectedMessage.TOPIC)
                            .historyItemSelected(historyList.vendor, obj)
                }
            }
        }
        val messageBusConnection = messageBus.connect()
        messageBusConnection.subscribe(CreateHistoryMessage.TOPIC, this)
        messageBusConnection.subscribe(HistoryPanelMessages.TOPIC, this)
    }

    override fun createHistoryItem(vendor: DatabaseVendor, historyItem: HistoryItem) {
        if (historyItem.isNotEmpty()) {
            val noSqlConfiguration = NoSqlConfiguration.getInstance(project)
            val historyList = noSqlConfiguration.findListByVendor(vendor, project)
            historyList.addItem(historyItem, project)
        }
    }

    override fun add(historyList: HistoryList) {
        val databaseTree = databaseTree ?: return
        if (findHistoryListNode(historyList.vendor) == null) {
            if (databaseTree.model == null) {
                databaseTree.model = DefaultTreeModel(DefaultMutableTreeNode())
            }
            databaseTree.rootNode?.add(DefaultMutableTreeNode(historyList))
        }
    }

    override fun add(vendorName: String, index: Int, element: HistoryItem) {
        findHistoryListNode(vendorName)?.add(DefaultMutableTreeNode(element))
    }

    override fun removeAt(vendorName: String, index: Int) {
        findHistoryListNode(vendorName)?.remove(index)
    }

    override fun dispose() {
        project.messageBus.dispose()
        super.dispose()
    }

    private fun historyList(treeNode: DefaultMutableTreeNode?): HistoryList? {
        if (treeNode != null) {
            val obj = treeNode.userObject
            if (obj is HistoryList) {
                return obj
            }
            return historyList(treeNode.parent as DefaultMutableTreeNode)
        }
        return null
    }

    override fun initializeTree(databaseTree: ExplorerTree) {
        val configuration = NoSqlConfiguration.getInstance(project)
        val rootNode = DefaultMutableTreeNode()
        configuration.historyLists
                .forEach {
                    val items = it.items
                    if (items.isNotEmpty()) {
                        val listRoot = DefaultMutableTreeNode(it)
                        rootNode.add(listRoot)
                        items.forEach {
                            listRoot.add(DefaultMutableTreeNode(it))
                        }
                    }
                }
        databaseTree.model =
                if (rootNode.isLeaf) {
                    null
                } else {
                    DefaultTreeModel(rootNode)
                }
    }

    private fun findHistoryListNode(vendorName: String): DefaultMutableTreeNode? {
        return databaseTree?.rootNode
                ?.children()
                ?.toList()
                ?.map { it as DefaultMutableTreeNode }
                ?.firstOrNull {
                    (it.userObject as HistoryList).vendor == vendorName
                }
    }

    fun pinSelectedHistoryItem() {
    }

    fun isHistoryItemSelected() = false

    fun isSelectedHistoryItemPinned() = false
}