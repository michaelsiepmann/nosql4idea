package org.codinjutsu.tools.nosql.commons.model.explorer

import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

abstract class FolderDatabaseServerFolder<SERVERCONFIGURATION : ServerConfiguration, DOCUMENT> constructor(databaseserver: DatabaseServer<SERVERCONFIGURATION>, private val databaseVendorClientManager: DatabaseVendorClientManager) :
        DatabaseServerFolder<SERVERCONFIGURATION>(databaseserver) {

    override val isDatabaseWithCollections: Boolean
        get() = databaseClient.isDatabaseWithCollections

    internal val databaseClient: DatabaseClient<Database, DOCUMENT, SERVERCONFIGURATION>
        get() = databaseVendorClientManager.getClient(configuration.databaseVendor) as DatabaseClient<Database, DOCUMENT, SERVERCONFIGURATION>
}
