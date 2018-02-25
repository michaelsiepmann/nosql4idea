package org.codinjutsu.tools.nosql.commons.view.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class RunScriptAction(private val noSqlExplorerPanel: DatabasePanel) :
        AnAction(getResourceString("action.runscript.text"), getResourceString("action.runscript.description"), AllIcons.General.Run) {

    override fun actionPerformed(event: AnActionEvent?) {
        noSqlExplorerPanel.runScript()
    }
}