package org.codinjutsu.tools.nosql.elasticsearch.view.panel.query

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.components.panels.NonOpaquePanel
import org.codinjutsu.tools.nosql.commons.view.createJSONEditor
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel

internal class ElasticsearchQueryPanel(private val project: Project) : JPanel(), Disposable, QueryPanel {

    private val filterEditor = createJSONEditor(project)

    init {
        layout = BorderLayout()
        val panel = JPanel()
        panel.layout = BorderLayout()
        val headPanel = NonOpaquePanel()
        val operatorLabel = JLabel("Filter")
        headPanel.add(operatorLabel, BorderLayout.WEST)
        panel.add(headPanel, BorderLayout.NORTH)
        panel.add(filterEditor.component, BorderLayout.CENTER)
        panel.maximumSize = Dimension(Short.MAX_VALUE.toInt(), Short.MAX_VALUE.toInt())
        add(panel)
        Disposer.register(project, this)
    }

    override fun getQueryOptions(rowLimit: String, page: Page?): QueryOptions {
        val limit =
                if (rowLimit.isNotBlank()) {
                    rowLimit.toInt()
                } else {
                    0
                }

        return ElasticsearchQueryOptions(limit, filterEditor.document.text, page)
    }

    override fun validateQuery() {
    }

    override fun requestFocusOnEditor() {
        val focusManager = IdeFocusManager.getInstance(project)
        if (focusManager.focusOwner !== filterEditor.component) {
            focusManager.requestFocus(filterEditor.component, true)
        }
    }

    override fun getComponent() = this

    override fun dispose() {
        val editorFactory = EditorFactory.getInstance()
        editorFactory.releaseEditor(filterEditor)
    }
}