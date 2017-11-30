package org.codinjutsu.tools.nosql.commons.view.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import org.codinjutsu.tools.nosql.NoSqlExplorerPanel
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class AddCollectionAction(private val noSqlExplorerPanel: NoSqlExplorerPanel) : AnAction(getResourceString("action.addcollection.text"), getResourceString("action.addcollection.description"), AllIcons.General.Add), DumbAware {

    override fun actionPerformed(e: AnActionEvent?) {
        noSqlExplorerPanel.createCollection()
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isVisible = noSqlExplorerPanel.isDatabaseWithCollections
    }
}