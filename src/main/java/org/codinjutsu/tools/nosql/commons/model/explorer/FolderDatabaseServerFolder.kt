package org.codinjutsu.tools.nosql.commons.model.explorer

import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection

abstract class FolderDatabaseServerFolder<SERVERCONFIGURATION : ServerConfiguration, COLLECTION : NoSQLCollection> constructor(databaseserver: DatabaseServer<SERVERCONFIGURATION>, private val databaseVendorClientManager: DatabaseVendorClientManager) :
        DatabaseServerFolder<SERVERCONFIGURATION, COLLECTION>(databaseserver) {

    override val isDatabaseWithCollections: Boolean
        get() = databaseClient.isDatabaseWithCollections

    internal val databaseClient: DatabaseClient<Database, COLLECTION, SERVERCONFIGURATION, COLLECTION>
        get() = databaseVendorClientManager.getClient(configuration.databaseVendor) as DatabaseClient<Database, COLLECTION, SERVERCONFIGURATION, COLLECTION>
}
