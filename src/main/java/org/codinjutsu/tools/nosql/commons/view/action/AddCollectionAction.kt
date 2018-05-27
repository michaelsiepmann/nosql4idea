package org.codinjutsu.tools.nosql.commons.view.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.view.explorer.DatabaseListPanel
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class AddCollectionAction(private val databaseListPanel: DatabaseListPanel) : AnAction(getResourceString("action.addcollection.text"), getResourceString("action.addcollection.description"), AllIcons.General.Add), DumbAware {

    override fun actionPerformed(e: AnActionEvent?) {
        databaseListPanel.createCollection()
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isVisible = databaseListPanel.canCreateChildAtSelectedFolder(FolderType.MONGO_COLLECTION) ||
                databaseListPanel.canCreateChildAtSelectedFolder(FolderType.ELASTICSEARCH_TYPE)
    }
}