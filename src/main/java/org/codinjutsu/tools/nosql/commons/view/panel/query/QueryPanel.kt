package org.codinjutsu.tools.nosql.commons.view.panel.query

import com.intellij.lang.Language
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.EditorSettings
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.PlainTextSyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.panels.NonOpaquePanel
import com.intellij.util.Alarm
import com.intellij.util.ui.UIUtil
import com.mongodb.util.JSON
import com.mongodb.util.JSONParseException
import org.apache.commons.lang.StringUtils
import org.codinjutsu.tools.nosql.commons.view.action.OperatorCompletionAction
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Font
import java.awt.Point
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextPane

internal class QueryPanel(private val project: Project) : JPanel(), Disposable {

    private val myUpdateAlarm = Alarm(Alarm.ThreadToUse.SWING_THREAD)
    private val queryCardLayout = CardLayout()

    private val mainPanel = JPanel()
    private val queryContainerPanel = JPanel()

    private val filterPanel: OperatorPanel
    private val aggregationPanel: OperatorPanel

    init {
        layout = BorderLayout()
        add(mainPanel)
        mainPanel.add(queryContainerPanel)

        queryContainerPanel.layout = queryCardLayout

        filterPanel = FilterPanel(project, this)
        queryContainerPanel.add(filterPanel, FILTER_PANEL)

        aggregationPanel = AggregatorPanel(project, this)
        queryContainerPanel.add(aggregationPanel, AGGREGATION_PANEL)

        toggleToFind()

        Disposer.register(project, this)
    }

    fun requestFocusOnEditor() {// Code from requestFocus of EditorImpl
        val focusManager = IdeFocusManager.getInstance(this.project)
        val editorContentComponent = getCurrentOperatorPanel().getRequestFocusComponent()
        if (focusManager.focusOwner !== editorContentComponent) {
            focusManager.requestFocus(editorContentComponent, true)
        }
    }

    private fun getCurrentOperatorPanel() =
            if (filterPanel.isVisible) {
                filterPanel
            } else {
                aggregationPanel
            }

    private fun fillEditorSettings(editorSettings: EditorSettings) {
        editorSettings.apply {
            isWhitespacesShown = true
            isLineMarkerAreaShown = false
            isIndentGuidesShown = false
            isLineNumbersShown = false
            isAllowSingleLogicalLineFolding = true
            additionalColumnsCount = 0
            additionalLinesCount = 1
            isUseSoftWraps = true
            setUseTabCharacter(false)
            isCaretInsideTabs = false
            isVirtualSpace = false
        }
    }

    private fun attachHighlighter(editor: EditorEx) {
        val scheme = editor.colorsScheme
        scheme.setColor(EditorColors.CARET_ROW_COLOR, null)
        editor.highlighter = createHighlighter(scheme)
    }

    private fun createHighlighter(settings: EditorColorsScheme): EditorHighlighter {
        val language = Language.findLanguageByID("JSON") ?: Language.ANY
        return LexerEditorHighlighter(PlainTextSyntaxHighlighterFactory.getSyntaxHighlighter(language!!, null, null), settings)
    }

    fun getQueryOptions(rowLimit: String, page: Page?) = getCurrentOperatorPanel().buildQueryOptions(rowLimit, page)

    override fun dispose() {
        myUpdateAlarm.cancelAllRequests()
        filterPanel.dispose()
        aggregationPanel.dispose()
    }

    fun toggleToAggregation() {
        queryCardLayout.show(queryContainerPanel, AGGREGATION_PANEL)
    }

    fun toggleToFind() {
        queryCardLayout.show(queryContainerPanel, FILTER_PANEL)
    }

    fun validateQuery() {
        getCurrentOperatorPanel().validateQuery()
    }

    internal abstract class OperatorPanel(private val project: Project, internal val queryPanel: QueryPanel) : JPanel(), Disposable {

        abstract fun getRequestFocusComponent(): JComponent

        abstract fun validateQuery()

        abstract fun buildQueryOptions(rowLimit: String, page: Page?): QueryOptions

        internal fun notifyOnErrorForOperator(component: JComponent, ex: Exception) {
            val message = if (ex is JSONParseException) {
                StringUtils.removeStart(ex.message, "\n")
            } else {
                String.format("%s: %s", ex.javaClass.simpleName, ex.message)
            }
            val nonOpaquePanel = NonOpaquePanel()
            val textPane = Messages.configureMessagePaneUi(JTextPane(), message)
            textPane.font = QueryPanel.Companion.COURIER_FONT
            textPane.background = MessageType.ERROR.popupBackground
            nonOpaquePanel.add(textPane, BorderLayout.CENTER)
            nonOpaquePanel.add(JLabel(MessageType.ERROR.defaultIcon), BorderLayout.WEST)

            JBPopupFactory.getInstance().createBalloonBuilder(nonOpaquePanel)
                    .setFillColor(MessageType.ERROR.popupBackground)
                    .createBalloon()
                    .show(RelativePoint(component, Point(0, 0)), Balloon.Position.above)
        }

        protected fun createEditor(): Editor {
            val editorFactory = EditorFactory.getInstance()
            val editorDocument = editorFactory.createDocument("")
            val editor = editorFactory.createEditor(editorDocument, project)
            queryPanel.fillEditorSettings(editor.settings)
            val editorEx = editor as EditorEx
            queryPanel.attachHighlighter(editorEx)

            return editor
        }
    }

    private inner class AggregatorPanel(project: Project, queryPanel: QueryPanel) : OperatorPanel(project, queryPanel) {

        private val editor: Editor = createEditor()
        private val operatorCompletionAction: OperatorCompletionAction

        private val query: String
            get() = String.format("[%s]", StringUtils.trim(this.editor.document.text))

        init {
            layout = BorderLayout()
            val headPanel = NonOpaquePanel()
            val operatorLabel = JLabel("Aggregation")
            headPanel.add(operatorLabel, BorderLayout.WEST)
            add(headPanel, BorderLayout.NORTH)
            add(this.editor.component, BorderLayout.CENTER)

            operatorCompletionAction = OperatorCompletionAction(project, editor)


            myUpdateAlarm.setActivationComponent(this.editor.component)
        }

        override fun validateQuery() {
            try {
                val query = query
                if (StringUtils.isEmpty(query)) {
                    return
                }
                JSON.parse(query)
            } catch (ex: JSONParseException) {
                notifyOnErrorForOperator(editor.component, ex)
            } catch (ex: NumberFormatException) {
                notifyOnErrorForOperator(editor.component, ex)
            }

        }

        override fun buildQueryOptions(rowLimit: String, page: Page?): QueryOptions {
            val queryOptions = QueryOptionsImpl(page = page)
            try {
                queryOptions.operations = query
            } catch (ex: JSONParseException) {
                notifyOnErrorForOperator(editor.component, ex)
            }

            if (StringUtils.isNotBlank(rowLimit)) {
                queryOptions.resultLimit = Integer.parseInt(rowLimit)
            }

            return queryOptions
        }

        override fun getRequestFocusComponent() = this.editor.contentComponent

        override fun dispose() {
            operatorCompletionAction.dispose()
            EditorFactory.getInstance().releaseEditor(this.editor)
        }
    }

    private class FilterPanel(project: Project, queryPanel: QueryPanel) : OperatorPanel(project, queryPanel) {

        private val selectEditor: Editor
        private val operatorCompletionAction: OperatorCompletionAction
        private val projectionEditor: Editor
        private val sortEditor: Editor

        init {
            layout = BoxLayout(this, BoxLayout.X_AXIS)

            this.selectEditor = createEditor()
            this.operatorCompletionAction = OperatorCompletionAction(project, selectEditor)
            add(createSubOperatorPanel("Filter", this.selectEditor))

            this.projectionEditor = createEditor()
            add(createSubOperatorPanel("Projection", this.projectionEditor))

            this.sortEditor = createEditor()
            add(createSubOperatorPanel("Sort", this.sortEditor))
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

            if (StringUtils.isNotBlank(rowLimit)) {
                queryOptions.resultLimit = Integer.parseInt(rowLimit)
            }

            return queryOptions
        }

        override fun dispose() {
            operatorCompletionAction.dispose()
            val editorFactory = EditorFactory.getInstance()
            editorFactory.releaseEditor(selectEditor)
            editorFactory.releaseEditor(projectionEditor)
            editorFactory.releaseEditor(sortEditor)
        }

        private fun validateEditorQuery(editor: Editor) {
            try {
                val query = getQueryFrom(editor)
                if (StringUtils.isNotEmpty(query)) {
                    JSON.parse(query)
                }
            } catch (ex: JSONParseException) {
                notifyOnErrorForOperator(editor.component, ex)
            } catch (ex: NumberFormatException) {
                notifyOnErrorForOperator(editor.component, ex)
            }
        }

        private fun getQueryFrom(editor: Editor) = StringUtils.trim(editor.document.text)

        private fun createSubOperatorPanel(title: String, subOperatorEditor: Editor): JPanel {
            val selectPanel = JPanel()
            selectPanel.layout = BorderLayout()
            val headPanel = NonOpaquePanel()
            val operatorLabel = JLabel(title)
            headPanel.add(operatorLabel, BorderLayout.WEST)
            selectPanel.add(headPanel, BorderLayout.NORTH)
            selectPanel.add(subOperatorEditor.component, BorderLayout.CENTER)

            queryPanel.myUpdateAlarm.setActivationComponent(subOperatorEditor.component)

            return selectPanel
        }
    }

    companion object {
        internal val COURIER_FONT = Font("Courier", Font.PLAIN, UIUtil.getLabelFont().size)

        private val FILTER_PANEL = "FilterPanel"
        private val AGGREGATION_PANEL = "AggregationPanel"
    }
}