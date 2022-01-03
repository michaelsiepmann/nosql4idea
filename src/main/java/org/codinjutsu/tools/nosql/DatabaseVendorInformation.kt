package org.codinjutsu.tools.nosql

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.DatabaseUI
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.commons.view.console.AbstractNoSQLConsoleRunner

internal abstract class DatabaseVendorInformation(val hasConsoleWindow: Boolean = false) {

    open fun createConsoleRunner(project: Project, configuration: ServerConfiguration, database: Database): AbstractNoSQLConsoleRunner? =
            null

    abstract fun createDatabaseServerFolder(databaseServer: DatabaseServer, project: Project): DatabaseServerFolder<*>

    abstract fun getDatabaseUIClass(): Class<out DatabaseUI>

    fun getClient(project: Project): DatabaseClient? {
        return project.getService(getDatabaseClientClass() as Class<DatabaseClient>)
    }

    protected abstract fun getDatabaseClientClass(): Class<out DatabaseClient>

    abstract fun availableDataTypes(): Array<DataType>
}