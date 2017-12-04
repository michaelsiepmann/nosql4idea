package org.codinjutsu.tools.nosql.mongo.view.panel.query

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.util.Alarm
import com.intellij.util.ui.UIUtil
import org.codinjutsu.tools.nosql.commons.view.action.EnableAggregateAction
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Font
import javax.swing.JPanel

internal class MongoQueryPanel(private val project: Project) : JPanel(), Disposable, QueryPanel {

    private val myUpdateAlarm = Alarm(Alarm.ThreadToUse.SWING_THREAD)
    private val queryCardLayout = CardLayout()

    private val queryContainerPanel = JPanel()

    private val filterPanel: OperatorPanel
    private val aggregationPanel: OperatorPanel

    init {
        layout = BorderLayout()
        add(queryContainerPanel)

        queryContainerPanel.layout = queryCardLayout

        filterPanel = FilterPanel(project) {
            myUpdateAlarm.setActivationComponent(it)
        }
        queryContainerPanel.add(filterPanel, FILTER_PANEL)

        aggregationPanel = AggregatorPanel(project) {
            myUpdateAlarm.setActivationComponent(it)
        }
        queryContainerPanel.add(aggregationPanel, AGGREGATION_PANEL)

        toggleToFind()

        Disposer.register(project, this)
    }

    override fun addActions(actionResultGroup: DefaultActionGroup) {
        actionResultGroup.add(EnableAggregateAction(this))
    }

    override fun requestFocusOnEditor() {// Code from requestFocus of EditorImpl
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

    override fun getQueryOptions(rowLimit: String, page: Page?) = getCurrentOperatorPanel().buildQueryOptions(rowLimit, page)

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

    override fun validateQuery() {
        getCurrentOperatorPanel().validateQuery()
    }

    override fun getComponent() = this

    companion object {
        internal val COURIER_FONT = Font("Courier", Font.PLAIN, UIUtil.getLabelFont().size)

        private val FILTER_PANEL = "FilterPanel"
        private val AGGREGATION_PANEL = "AggregationPanel"
    }
}