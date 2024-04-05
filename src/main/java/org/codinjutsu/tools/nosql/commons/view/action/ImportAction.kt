package org.codinjutsu.tools.nosql.commons.view.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel

internal class ImportAction(private val databasePanel: DatabasePanel) : AnAction(AllIcons.Actions.MenuOpen) {

    override fun actionPerformed(e: AnActionEvent) {
        databasePanel.startImport()
    }
}