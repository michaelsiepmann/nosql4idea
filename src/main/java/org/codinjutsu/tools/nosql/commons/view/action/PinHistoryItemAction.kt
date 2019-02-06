package org.codinjutsu.tools.nosql.commons.view.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Toggleable
import org.codinjutsu.tools.nosql.NoSqlTreeRenderer
import org.codinjutsu.tools.nosql.commons.view.explorer.HistoryListPanel

internal class PinHistoryItemAction(private val historyListPanel: HistoryListPanel) : AnAction("Pin item", "", NoSqlTreeRenderer.ICON_PINNED) {
    override fun actionPerformed(e: AnActionEvent) {
        historyListPanel.pinSelectedHistoryItem()
    }

    override fun update(e: AnActionEvent) {
        val enabled = historyListPanel.isHistoryItemSelected()
        val selected = enabled && historyListPanel.isSelectedHistoryItemPinned()
        e.presentation.isEnabled = enabled
        e.presentation.putClientProperty(Toggleable.SELECTED_PROPERTY, selected)
    }
}