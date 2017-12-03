package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.JBColor
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

abstract class DatabaseServerFolder<SERVERCONFIGURATION : ServerConfiguration> constructor(override val data: DatabaseServer<SERVERCONFIGURATION>) : Folder<DatabaseServer<SERVERCONFIGURATION>> {
    override val name: String?
        get() = data.label

    val configuration: SERVERCONFIGURATION
        get() = data.configuration

    override val databaseServer: DatabaseServer<*>
        get() = data

    open val isDatabaseWithCollections: Boolean
        get() = false

    open fun dropDatabase(database: Database) {}

    open fun createCollection(selectedDatabase: Database): Any? = null

    override val children: Collection<Folder<*>>
        get() = data.databases.map { createDatabaseFolder(it) }

    internal abstract fun createDatabaseFolder(database: Database): DatabaseFolder

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            val label = databaseServer.label
            val host = databaseServer.serverUrl
            append(if (label.isBlank()) host else label)

            if (DatabaseServer.Status.OK == databaseServer.status) {
                toolTipText = host
                icon = databaseServer.vendor.icon
            } else {
                foreground = JBColor.RED
                toolTipText = "Unable to connect"
            }
        }
    }
}
