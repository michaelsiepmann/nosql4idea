package org.codinjutsu.tools.nosql.mongo.view.panel.query

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
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.panels.NonOpaquePanel
import com.mongodb.util.JSON
import com.mongodb.util.JSONParseException
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import java.awt.BorderLayout
import java.awt.Point
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextPane

internal abstract class OperatorPanel(private val project: Project) : JPanel(), Disposable {

    abstract fun getRequestFocusComponent(): JComponent

    abstract fun validateQuery()

    abstract fun buildQueryOptions(rowLimit: String, page: Page?): QueryOptions

    internal fun notifyOnErrorForOperator(component: JComponent, ex: Exception) {
        val message = if (ex is JSONParseException) {
            ex.message?.removePrefix("\n") ?: ""
        } else {
            String.format("%s: %s", ex.javaClass.simpleName, ex.message)
        }
        val nonOpaquePanel = NonOpaquePanel()
        val textPane = Messages.configureMessagePaneUi(JTextPane(), message)
        textPane.font = MongoQueryPanel.COURIER_FONT
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
        fillEditorSettings(editor.settings)
        val editorEx = editor as EditorEx
        attachHighlighter(editorEx)
        return editor
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

    protected fun validateQuery(query: String, editor: Editor) {
        try {
            if (query.isNotEmpty()) {
                JSON.parse(query)
            }
        } catch (ex: JSONParseException) {
            notifyOnErrorForOperator(editor.component, ex)
        } catch (ex: NumberFormatException) {
            notifyOnErrorForOperator(editor.component, ex)
        }
    }
}