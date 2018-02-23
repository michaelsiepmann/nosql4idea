package org.codinjutsu.tools.nosql.commons.view.scripting

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.impl.status.TextPanel
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import javax.script.ScriptContext
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext
import javax.swing.JScrollPane

internal class JavascriptExecutor<CONTEXT : DatabaseContext<*>, CLIENT : DatabaseClient<CONTEXT, *, *>>(
        private val script: String,
        private val project: Project,
        private val wrapper: ScriptingDatabaseWrapper<*>,
        private val context: CONTEXT,
        private val client: CLIENT
) {

    fun execute() {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val window = toolWindowManager.registerToolWindow("org.codinjutsu.tools.nosql.window", true, ToolWindowAnchor.BOTTOM)
        val contentManager = window.contentManager
        val textArea = MyTextPanel()
        val content = contentManager.factory.createContent(JScrollPane(textArea), "test", false)
        content.isCloseable = true
        content.tabName = "Test"
        contentManager.addContent(content)
        val consoleLog = ConsoleLog(textArea)
        window.show {
            val searchResult = client.findAll(context)
            for (it in searchResult.records) {
                val engine = ScriptEngineManager().getEngineByName("JavaScript")
                val scriptContext = SimpleScriptContext()
                val bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE)
                bindings["data"] = it
                bindings["database"] = wrapper
                bindings["console"] = consoleLog
                engine.eval("main(data, database, console);\n" + script, scriptContext)
            }
        }
 //       toolWindowManager.unregisterToolWindow("org.con.nosql.window")
    }

    internal class MyTextPanel : TextPanel() {

        fun append(text: String) {
            setText(getText() + text)
        }
    }
}