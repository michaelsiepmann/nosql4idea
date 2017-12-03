package org.codinjutsu.tools.nosql.commons.model.explorer

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

abstract class DatabaseServerFolder<SERVERCONFIGURATION : ServerConfiguration, COLLECTION : NoSQLCollection> constructor(override val data: DatabaseServer<SERVERCONFIGURATION>) : Folder<DatabaseServer<SERVERCONFIGURATION>> {

    override val name: String?
        get() = data.label

    val configuration: SERVERCONFIGURATION
        get() = data.configuration

    override val databaseServer: DatabaseServer<*>
        get() = data

    open val isDatabaseWithCollections: Boolean
        get() = false

    open fun dropDatabase(database: Database) {}

    open fun createCollection(selectedDatabase: Database): COLLECTION? = null

    open fun dropCollection(selectedCollection: COLLECTION) {}

    override val children: Collection<Folder<*>>
        get() = data.databases.map { createDatabaseFolder(it) }

    internal abstract fun createDatabaseFolder(database: Database): DatabaseFolder
}
