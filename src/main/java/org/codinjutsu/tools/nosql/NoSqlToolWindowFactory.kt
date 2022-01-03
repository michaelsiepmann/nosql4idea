package org.codinjutsu.tools.nosql

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import org.codinjutsu.tools.nosql.commons.view.explorer.NoSqlExplorerPanel

internal class NoSqlToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val noSqlExplorerPanel =
            NoSqlExplorerPanel(project, project.getService(DatabaseVendorClientManager::class.java))
        noSqlExplorerPanel.installActions()
        val application = ApplicationManager.getApplication()
        val nosqlExplorer =
            application.getService(ContentFactory::class.java).createContent(noSqlExplorerPanel, null, false)
        toolWindow.contentManager.addContent(nosqlExplorer)
        //       toolWindow.title = NOSQL_EXPLORER
        application.invokeLater { noSqlExplorerPanel.initializeExplorerPanel() }
    }

    companion object {
        private const val NOSQL_EXPLORER = "NoSql Explorer"
    }
}
