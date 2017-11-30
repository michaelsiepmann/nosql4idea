package org.codinjutsu.tools.nosql.commons.logic

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

internal interface FolderDatabaseClient<in DATABASE : Database, COLLECTION : NoSQLCollection, SERVERCONFIGURATION : ServerConfiguration> {

    fun isDatabaseWithCollections() = false

    fun createFolder(serverConfiguration: SERVERCONFIGURATION, parentFolderName: String, folderName: String): COLLECTION

    fun dropFolder(serverConfiguration: SERVERCONFIGURATION, collection: COLLECTION)

    fun dropDatabase(serverConfiguration: SERVERCONFIGURATION, database: DATABASE)
}