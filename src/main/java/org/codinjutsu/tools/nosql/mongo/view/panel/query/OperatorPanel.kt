package org.codinjutsu.tools.nosql.mongo.view.panel.query

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.panels.NonOpaquePanel
import com.mongodb.BasicDBObject
import org.codinjutsu.tools.nosql.commons.history.HistoryItem
import org.codinjutsu.tools.nosql.commons.view.createJSONEditor
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

    abstract fun showHistoryItem(historyItem: HistoryItem)

    internal fun notifyOnErrorForOperator(component: JComponent, ex: Exception) {
        val message = String.format("%s: %s", ex.javaClass.simpleName, ex.message)
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
        return createJSONEditor(project)
    }

    protected fun validateQuery(query: String, editor: Editor) {
        try {
            if (query.isNotEmpty()) {
                BasicDBObject.parse(query)
            }
        } catch (ex: NumberFormatException) {
            notifyOnErrorForOperator(editor.component, ex)
        }
    }
}