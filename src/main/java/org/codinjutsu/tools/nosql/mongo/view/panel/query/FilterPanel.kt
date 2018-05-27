package org.codinjutsu.tools.nosql.mongo.view.panel.query

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.ui.components.panels.NonOpaquePanel
import com.mongodb.util.JSONParseException
import org.codinjutsu.tools.nosql.commons.history.HistoryItem
import org.codinjutsu.tools.nosql.commons.view.action.OperatorCompletionAction
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

internal class FilterPanel(project: Project, private val updateAlarm: (JComponent) -> Unit) : OperatorPanel(project) {

    private val selectEditor: Editor
    private val operatorCompletionAction: OperatorCompletionAction
    private val projectionEditor: Editor
    private val sortEditor: Editor

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)

        selectEditor = createEditor()
        operatorCompletionAction = OperatorCompletionAction(project, selectEditor)
        add(createSubOperatorPanel("Filter", selectEditor))

        projectionEditor = createEditor()
        add(createSubOperatorPanel("Projection", projectionEditor))

        sortEditor = createEditor()
        add(createSubOperatorPanel("Sort", sortEditor))
    }

    override fun getRequestFocusComponent() = this.selectEditor.contentComponent

    override fun validateQuery() {
        validateEditorQuery(selectEditor)
        validateEditorQuery(projectionEditor)
        validateEditorQuery(sortEditor)
    }

    override fun buildQueryOptions(rowLimit: String, page: Page?): QueryOptions {
        val queryOptions = QueryOptionsImpl()
        try {
            queryOptions.filter = getQueryFrom(selectEditor)
            queryOptions.projection = getQueryFrom(projectionEditor)
            queryOptions.sort = getQueryFrom(sortEditor)
        } catch (ex: JSONParseException) {
            notifyOnErrorForOperator(selectEditor.component, ex)
        }

        if (rowLimit.isNotBlank()) {
            queryOptions.resultLimit = rowLimit.toInt()
        }

        return queryOptions
    }

    override fun showHistoryItem(historyItem: HistoryItem) {
        selectEditor.document.setText(historyItem.filter)
    }

    override fun dispose() {
        operatorCompletionAction.dispose()
        val editorFactory = EditorFactory.getInstance()
        editorFactory.releaseEditor(selectEditor)
        editorFactory.releaseEditor(projectionEditor)
        editorFactory.releaseEditor(sortEditor)
    }

    private fun validateEditorQuery(editor: Editor) {
        validateQuery(getQueryFrom(editor), editor)
    }

    private fun getQueryFrom(editor: Editor) = editor.document.text.trim()

    private fun createSubOperatorPanel(title: String, subOperatorEditor: Editor): JPanel {
        val selectPanel = JPanel()
        selectPanel.layout = BorderLayout()
        val headPanel = NonOpaquePanel()
        val operatorLabel = JLabel(title)
        headPanel.add(operatorLabel, BorderLayout.WEST)
        selectPanel.add(headPanel, BorderLayout.NORTH)
        selectPanel.add(subOperatorEditor.component, BorderLayout.CENTER)

        updateAlarm(subOperatorEditor.component)

        selectPanel.maximumSize = Dimension(Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
        return selectPanel
    }
}