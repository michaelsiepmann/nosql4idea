package org.codinjutsu.tools.nosql.commons.view.scripting

import com.intellij.execution.process.ProcessHandler
import java.io.OutputStream

internal class ScriptProcessHandler(private val out: OutputStream) : ProcessHandler() {
    override fun getProcessInput() = out

    override fun detachIsDefault() = true

    override fun detachProcessImpl() {
    }

    override fun destroyProcessImpl() {
    }
}