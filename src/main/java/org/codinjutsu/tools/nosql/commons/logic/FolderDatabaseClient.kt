package org.codinjutsu.tools.nosql.commons.logic

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

internal interface FolderDatabaseClient<DATABASE : Database, COLLECTION : NoSQLCollection> {

    fun dropFolder(serverConfiguration: ServerConfiguration, collection: COLLECTION)

    fun dropDatabase(serverConfiguration: ServerConfiguration, database: DATABASE)
}