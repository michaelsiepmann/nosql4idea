package org.codinjutsu.tools.nosql.commons.model.explorer

import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

abstract class FolderDatabaseServerFolder<DOCUMENT> constructor(databaseserver: DatabaseServer, private val databaseVendorClientManager: DatabaseVendorClientManager) :
        DatabaseServerFolder(databaseserver) {

    override val isDatabaseWithCollections: Boolean
        get() = databaseClient.isDatabaseWithCollections

    internal val databaseClient: DatabaseClient<Database, DOCUMENT>
        get() = databaseVendorClientManager.getClient(configuration.databaseVendor) as DatabaseClient<Database, DOCUMENT>
}
