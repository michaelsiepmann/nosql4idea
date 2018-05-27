package org.codinjutsu.tools.nosql.commons.view.explorer

import com.intellij.ide.CommonActionsManager
import com.intellij.ide.TreeExpander
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.tree.TreeUtil
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import javax.swing.JPanel
import javax.swing.tree.DefaultMutableTreeNode

internal abstract class AbstractListPanel : JPanel(), Disposable {

    protected val toolBarPanel = JPanel()
    protected val treePanel = JPanel()
    protected var databaseTree: ExplorerTree? = ExplorerTree()

    init {
        layout = BorderLayout()
        add(toolBarPanel, NORTH)
        add(treePanel, CENTER)

        toolBarPanel.layout = BorderLayout()
        treePanel.layout = BorderLayout()

        treePanel.add(JBScrollPane(databaseTree), CENTER)
        databaseTree?.isRootVisible = false

        ApplicationManager.getApplication().invokeLater { this.initializeTree() }
    }

    override fun dispose() {
        databaseTree = null
    }

    fun installActions() {
        installActions(databaseTree!!)
    }

    abstract fun installActions(databaseTree: ExplorerTree)

    private fun expandAll() {
        TreeUtil.expandAll(databaseTree!!)
    }

    private fun collapseAll() {
        TreeUtil.collapseAll(databaseTree!!, 1)
    }

    protected fun createExpandCollapseButtons(): Pair<AnAction, AnAction> {
        val treeExpander = object : TreeExpander {
            override fun expandAll() {
                this@AbstractListPanel.expandAll()
            }

            override fun canExpand(): Boolean {
                return true
            }

            override fun collapseAll() {
                this@AbstractListPanel.collapseAll()
            }

            override fun canCollapse(): Boolean {
                return true
            }
        }

        val actionsManager = CommonActionsManager.getInstance()

        val expandAllAction = actionsManager.createExpandAllAction(treeExpander, treePanel)
        val collapseAllAction = actionsManager.createCollapseAllAction(treeExpander, treePanel)

        Disposer.register(this, Disposable {
            collapseAllAction.unregisterCustomShortcutSet(treePanel)
            expandAllAction.unregisterCustomShortcutSet(treePanel)
        })

        return Pair(expandAllAction, collapseAllAction)
    }

    fun initializeTree() {
        initializeTree(databaseTree!!)
    }

    protected abstract fun initializeTree(databaseTree: ExplorerTree)

    protected fun <T> getSelectedItem(): T? {
        val treeNode = databaseTree!!.lastSelectedPathComponent as DefaultMutableTreeNode?
        return if (treeNode != null) {
            treeNode.userObject as T
        } else {
            null
        }
    }
}