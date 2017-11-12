package org.codinjutsu.tools.nosql.commons.view.console

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.console.ConsoleHistoryController
import com.intellij.execution.console.ConsoleRootType
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import org.apache.commons.lang.StringUtils
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.NoSqlConfiguration
import org.codinjutsu.tools.nosql.ServerConfiguration

internal abstract class AbstractNoSQLConsoleRunner(project: Project, consoleTitle: String, workingDir: String, val serverConfiguration: ServerConfiguration) :
        AbstractConsoleRunnerWithHistory<NoSqlConsoleView>(project, consoleTitle, workingDir) {

    override fun createConsoleView(): NoSqlConsoleView {
        val res = NoSqlConsoleView(project, getShellConsoleTitle(), serverConfiguration)

        val file = res.file
        assert(file.context == null)
        file.putUserData<Boolean>(getShellFile(), java.lang.Boolean.TRUE)

        return res
    }

    override fun createProcessHandler(process: Process): OSProcessHandler {
        return OSProcessHandler(process, null)
    }

    override fun createExecuteActionHandler(): ProcessBackedConsoleExecuteActionHandler {
        val handler = object : ProcessBackedConsoleExecuteActionHandler(processHandler, false) {
            override fun getEmptyExecuteAction(): String {
                return "NoSql.Shell.Execute"
            }
        }
        ConsoleHistoryController(object : ConsoleRootType(getConsoleTypeId(), null) {

        }, null, consoleView).install()
        return handler
    }

    protected abstract fun getConsoleTypeId(): String

    protected abstract fun getShellConsoleTitle(): String

    protected abstract fun getShellFile(): Key<Boolean>

    @Throws(ExecutionException::class)
    override fun createProcess(): Process? {
        val commandLine = GeneralCommandLine()
        commandLine.exePath = NoSqlConfiguration.getInstance(project).getShellPath(getDatabaseVendor())
        return createProcess(commandLine, serverConfiguration)
    }

    protected abstract fun getDatabaseVendor(): DatabaseVendor

    @Throws(ExecutionException::class)
    protected abstract fun createProcess(commandLine: GeneralCommandLine, serverConfiguration: ServerConfiguration): Process

    protected fun addCommandlineParameter(commandLine: GeneralCommandLine, parameterKey: String, parameterValue: String) {
        if (StringUtils.isNotBlank(parameterValue)) {
            commandLine.addParameter(parameterKey)
            commandLine.addParameter(parameterValue)
        }
    }

    protected fun setWorkingDirectory(commandLine: GeneralCommandLine, serverConfiguration: ServerConfiguration) {
        val shellWorkingDir = serverConfiguration.shellWorkingDir
        if (StringUtils.isNotBlank(shellWorkingDir)) {
            commandLine.withWorkDirectory(shellWorkingDir)
        }
    }

    protected fun addShellArguments(commandLine: GeneralCommandLine, serverConfiguration: ServerConfiguration) {
        val shellArgumentsLine = serverConfiguration.shellArgumentsLine
        if (StringUtils.isNotBlank(shellArgumentsLine)) {
            commandLine.addParameters(*shellArgumentsLine!!.split((" ").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())
        }
    }
}