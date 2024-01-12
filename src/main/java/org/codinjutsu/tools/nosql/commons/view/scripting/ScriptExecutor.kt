package org.codinjutsu.tools.nosql.commons.view.scripting

import com.intellij.ide.script.IdeScriptEngineManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.impl.status.TextPanel
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import javax.swing.JScrollPane

internal class ScriptExecutor(
        private val extension: String,
        private val script: String,
        private val project: Project,
        private val wrapper: ScriptingDatabaseWrapper,
        private val context: DatabaseContext
) {

    fun execute() {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val window = toolWindow(toolWindowManager)
        val contentManager = window.contentManager
        val textArea = MyTextPanel()
        val content = contentManager.factory.createContent(JScrollPane(textArea), "test", false)
        content.isCloseable = true
        content.tabName = "Test"
        contentManager.addContent(content)
        val consoleLog = ConsoleLog(textArea)
        window.show {
            val engine = IdeScriptEngineManager.getInstance().getEngineByFileExtension(extension, null)
            if (engine != null) {
                val searchResult = context.client.findAll(context)
                for (it in searchResult.records) {
                    engine.setBinding("data", it)
                    engine.setBinding("database", wrapper)
                    engine.setBinding("console", consoleLog)
                    engine.eval(script)
                }
            }
        }
        //toolWindowManager.unregisterToolWindow("org.codinjutsu.tools.nosql.window")
    }

    private fun toolWindow(toolWindowManager: ToolWindowManager): ToolWindow {
        return toolWindowManager.getToolWindow("org.codinjutsu.tools.nosql.window")
                ?: throw IllegalStateException("Toolwindow not found")
    }

    internal class MyTextPanel : TextPanel() {

        fun append(text: String) {
            this.text += text
        }
    }
}