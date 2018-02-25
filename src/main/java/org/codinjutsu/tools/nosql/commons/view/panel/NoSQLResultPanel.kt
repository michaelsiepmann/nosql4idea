package org.codinjutsu.tools.nosql.commons.view.panel

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.Splitter
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.util.Disposer
import com.intellij.ui.PopupHandler
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.tree.TreeUtil
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.ActionCallback
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.commons.view.EditionPanel
import org.codinjutsu.tools.nosql.commons.view.JsonTreeTableView
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.action.CopyResultAction
import org.codinjutsu.tools.nosql.commons.view.action.EditDocumentAction
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider.Companion.DONT_WRITE
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.buildTree
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JPanel
import javax.swing.tree.DefaultMutableTreeNode

internal open class NoSQLResultPanel(
        project: Project,
        private val databasePanel: DatabasePanel,
        val editable: Boolean,
        private val nodeDescriptorFactory: NodeDescriptorFactory,
        private val idDescriptorKey: String
) : JPanel(), Disposable {

    private val splitter: Splitter
    protected val resultTreePanel = JPanel(BorderLayout())
    private val editionPanel: EditionPanel?

    @Volatile
    var resultTableView: JsonTreeTableView? = null

    init {
        layout = BorderLayout()

        splitter = Splitter(true, 0.6f)
        splitter.firstComponent = resultTreePanel

        editionPanel = if (editable) {
            createEditionPanel()
        } else {
            null
        }

        add(splitter, BorderLayout.CENTER)

        Disposer.register(project, this)
    }

    private fun createEditionPanel(): EditionPanel? {
        val editionPanel = EditionPanel(nodeDescriptorFactory, writeableColumnInfoDecider())
        editionPanel.init(databasePanel, object : ActionCallback {
            override fun onOperationSuccess(message: String) {
                hideEditionPanel()
                GuiUtils.showNotification(resultTreePanel, MessageType.INFO, message, Balloon.Position.above)
            }

            override fun onOperationFailure(exception: Exception) {
                GuiUtils.showNotification(resultTreePanel, MessageType.ERROR, exception.message, Balloon.Position.above)
            }

            override fun onOperationCancelled(message: String) {
                hideEditionPanel()
            }
        })
        return editionPanel
    }

    protected open fun writeableColumnInfoDecider(): WriteableColumnInfoDecider = DONT_WRITE

    fun updateResultTableTree(searchResult: SearchResult) {
        resultTableView = createTableView(searchResult)
        with(resultTableView) {
            name = "resultTreeTable"

            if (editable) {
                addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(mouseEvent: MouseEvent?) {
                        if (mouseEvent!!.clickCount == 2 && isSelectedNodeId()) {
                            editSelectedDocument()
                        }
                    }
                })
            }
        }

        buildPopupMenu()

        resultTreePanel.invalidate()
        resultTreePanel.removeAll()
        resultTreePanel.add(JBScrollPane(resultTableView))
        resultTreePanel.validate()
    }

    protected open fun createTableView(searchResult: SearchResult) =
            JsonTreeTableView(buildTree(searchResult, nodeDescriptorFactory), JsonTreeTableView.KEY, JsonTreeTableView.READONLY_VALUE)

    protected open fun buildPopupMenu() {
        val actionPopupGroup = DefaultActionGroup("NoSQLResultPopupGroup", true)
        if (ApplicationManager.getApplication() != null) {
            actionPopupGroup.add(EditDocumentAction(this))
            actionPopupGroup.add(CopyResultAction(this))
        }

        PopupHandler.installPopupHandler(resultTableView, actionPopupGroup, "POPUP", ActionManager.getInstance())
    }

    fun editSelectedDocument() {
        val document = getSelectedDocument() ?: return
        editionPanel?.updateEditionTree(document)
        splitter.secondComponent = editionPanel
    }

    fun addDocument() {
        editionPanel?.updateEditionTree(null)
        splitter.secondComponent = editionPanel
    }

    private fun getSelectedDocument(): DatabaseElement? {
        val tree = resultTableView?.tree
        val treeNode = tree?.lastSelectedPathComponent as NoSqlTreeNode

        val descriptor = treeNode.descriptor
        if (descriptor is KeyValueDescriptor) {
            if (descriptor.key == idDescriptorKey) {
                val context = databasePanel.context
                val client: DatabaseClient<DatabaseElement> = context.client as DatabaseClient<DatabaseElement>
                return client.findDocument(context, descriptor.value!!);
            }
        }

        return null
    }

    fun isSelectedNodeId() =
            when {
                !editable -> false
                resultTableView != null && resultTableView!!.tree.lastSelectedPathComponent != null -> isSelectedNodeId(resultTableView!!.tree.lastSelectedPathComponent as NoSqlTreeNode)
                else -> false
            }

    private fun isSelectedNodeId(treeNode: NoSqlTreeNode): Boolean {
        val descriptor = treeNode.descriptor
        return descriptor is KeyValueDescriptor && descriptor.key == idDescriptorKey
    }

    fun expandAll() {
        TreeUtil.expandAll(resultTableView!!.tree)
    }

    fun collapseAll() {
        val tree = resultTableView!!.tree
        TreeUtil.collapseAll(tree, 1)
    }

    fun getSelectedNodeStringifiedValue(): String {
        val tree = resultTableView?.tree
        var lastSelectedResultNode: NoSqlTreeNode? = tree?.lastSelectedPathComponent as NoSqlTreeNode
        if (lastSelectedResultNode == null) {
            lastSelectedResultNode = tree.model.root as NoSqlTreeNode
        }
        val userObject = lastSelectedResultNode.descriptor
        return if (userObject is ResultDescriptor) {
            stringifyResult(lastSelectedResultNode)
        } else userObject.toString()
    }

    protected fun hideEditionPanel() {
        splitter.secondComponent = null
    }

    private fun stringifyResult(selectedResultNode: DefaultMutableTreeNode): String {
        return (0 until selectedResultNode.childCount)
                .asSequence()
                .map { selectedResultNode.getChildAt(it) as DefaultMutableTreeNode }
                .mapTo(LinkedList()) { it.userObject }
                .joinToString(", ", "[ ", " ]")
    }

    override fun dispose() {
        resultTableView = null
        editionPanel?.dispose()
    }
}