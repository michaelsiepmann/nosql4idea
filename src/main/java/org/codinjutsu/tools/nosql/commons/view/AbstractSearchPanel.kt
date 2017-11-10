package org.codinjutsu.tools.nosql.commons.view

import com.intellij.ide.CommonActionsManager
import com.intellij.ide.TreeExpander
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LoadingDecorator
import com.intellij.openapi.util.Disposer
import com.intellij.ui.NumberDocument
import com.intellij.ui.components.panels.NonOpaquePanel
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

internal abstract class AbstractSearchPanel<T>(val project: Project) : NoSqlResultView<T>() {

    protected abstract val toolBarPanel: JPanel
    protected abstract val errorPanel: JPanel
    protected abstract val mainPanel: JPanel
    protected abstract val containerPanel: JPanel

    private val rowLimitField = JTextField("")
    private val _resultPanel = JPanel(BorderLayout())
    private val loadingDecorator: LoadingDecorator = LoadingDecorator(resultPanel, this, 0)

    init {
        initToolbar()

        layout = BorderLayout()
        add(mainPanel)

        containerPanel.add(loadingDecorator.component)
    }

    private fun initToolbar() {
        toolBarPanel.layout = BorderLayout()

        rowLimitField.columns = 5
        rowLimitField.document = NumberDocument()
        rowLimitField.text = "100"

        val rowLimitPanel = NonOpaquePanel()
        rowLimitPanel.add(JLabel("Row limit:"), BorderLayout.WEST)
        rowLimitPanel.add(rowLimitField, BorderLayout.CENTER)
        rowLimitPanel.add(Box.createHorizontalStrut(5), BorderLayout.EAST)
        toolBarPanel.add(rowLimitPanel, BorderLayout.WEST)

        addCommonsActions()
    }

    private fun addCommonsActions() {
        val treeExpander = object : TreeExpander {
            override fun expandAll() {
                this@AbstractSearchPanel.expandAll()
            }

            override fun canExpand() = true

            override fun collapseAll() {
                this@AbstractSearchPanel.collapseAll()
            }

            override fun canCollapse() = true
        }

        val actionsManager = CommonActionsManager.getInstance()

        val expandAllAction = actionsManager.createExpandAllAction(treeExpander, resultPanel)
        val collapseAllAction = actionsManager.createCollapseAllAction(treeExpander, resultPanel)

        Disposer.register(this, Disposable {
            collapseAllAction.unregisterCustomShortcutSet(resultPanel)
            expandAllAction.unregisterCustomShortcutSet(resultPanel)
        })


        val actionResultGroup = DefaultActionGroup("CouchbaseResultGroup", true)
        actionResultGroup.add(ExecuteQuery(this))
        actionResultGroup.addSeparator()
        actionResultGroup.add(expandAllAction)
        actionResultGroup.add(collapseAllAction)

        val actionToolBar = ActionManager.getInstance().createActionToolbar("CouchbaseResultGroupActions", actionResultGroup, true)
        actionToolBar.layoutPolicy = ActionToolbar.AUTO_LAYOUT_POLICY
        val actionToolBarComponent = actionToolBar.component
        actionToolBarComponent.border = null
        actionToolBarComponent.isOpaque = false

        toolBarPanel.add(actionToolBarComponent, BorderLayout.CENTER)
    }

    protected abstract fun expandAll()

    protected abstract fun collapseAll()

    override fun getResultPanel() = _resultPanel

    private fun getLimit() = Integer.parseInt(rowLimitField.text)

    override fun executeQuery() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Executing query", true) { //TODO need to abstract this method
            override fun run(indicator: ProgressIndicator) {
                try {
                    loadAndDisplayResults(getLimit())
                } catch (ex: Exception) {
                    GuiUtils.runInSwingThread {
                        errorPanel.apply {
                            invalidate()
                            removeAll()
                            add(ErrorPanel(ex), BorderLayout.CENTER)
                            validate()
                            isVisible = true
                        }
                    }
                } finally {
                    GuiUtils.runInSwingThread { loadingDecorator.stopLoading() }
                }
            }
        })
    }

    @Throws(Exception::class)
    protected abstract fun loadAndDisplayResults(limit: Int)
}