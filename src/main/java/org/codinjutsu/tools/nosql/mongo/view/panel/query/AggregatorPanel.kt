package org.codinjutsu.tools.nosql.mongo.view.panel.query

import com.intellij.openapi.application.ApplicationManager
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
import javax.swing.JComponent
import javax.swing.JLabel

internal class AggregatorPanel(project: Project, updateAlarm: (JComponent) -> Unit) : OperatorPanel(project) {

    private val editor: Editor = createEditor()
    private val operatorCompletionAction: OperatorCompletionAction

    private val query: String
        get() = String.format("[%s]", editor.document.text).trim()

    init {
        layout = BorderLayout()
        val headPanel = NonOpaquePanel()
        val operatorLabel = JLabel("Aggregation")
        headPanel.add(operatorLabel, BorderLayout.WEST)
        add(headPanel, BorderLayout.NORTH)
        add(editor.component, BorderLayout.CENTER)
        operatorCompletionAction = OperatorCompletionAction(project, editor)
        updateAlarm(editor.component)
    }

    override fun validateQuery() {
        validateQuery(query, editor)
    }

    override fun buildQueryOptions(rowLimit: String, page: Page?): QueryOptions {
        val queryOptions = QueryOptionsImpl(page = page)
        try {
            queryOptions.operations = query
        } catch (ex: JSONParseException) {
            notifyOnErrorForOperator(editor.component, ex)
        }

        if (rowLimit.isNotBlank()) {
            queryOptions.resultLimit = rowLimit.toInt()
        }

        return queryOptions
    }

    override fun getRequestFocusComponent() = this.editor.contentComponent

    override fun showHistoryItem(historyItem: HistoryItem) {
        ApplicationManager.getApplication().runWriteAction {
            editor.document.setText(historyItem.filter)
        }
    }

    override fun dispose() {
        operatorCompletionAction.dispose()
        EditorFactory.getInstance().releaseEditor(this.editor)
    }
}