package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.JBColor
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

abstract class DatabaseServerFolder<DATABASE : Database> constructor(override val data: DatabaseServer) : Folder<DatabaseServer, DATABASE> {
    override val name: String?
        get() = data.label

    internal val configuration: ServerConfiguration
        get() = data.configuration

    override val databaseServer: DatabaseServer
        get() = data

    open val isDatabaseWithCollections: Boolean
        get() = false

    open fun dropDatabase(database: Database) {}

    open fun createCollection(selectedDatabase: Database): Any? = null

    override val children: Collection<Folder<*, DATABASE>>
        get() = data.databases.map { createDatabaseFolder(it) }

    internal abstract fun createDatabaseFolder(database: Database): DatabaseFolder<DATABASE>

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            val label = databaseServer.label
            val host = databaseServer.serverUrl
            append(if (label.isBlank()) host else label)

            icon = databaseServer.vendor.icon
            if (DatabaseServer.Status.OK == databaseServer.status) {
                toolTipText = host
            } else {
                foreground = JBColor.RED
                toolTipText = "Unable to connect"
            }
        }
    }
}
