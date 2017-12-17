package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

abstract class FolderDatabaseServerFolder<DOCUMENT> constructor(databaseserver: DatabaseServer, private val project: Project) :
        DatabaseServerFolder(databaseserver) {

    override val isDatabaseWithCollections: Boolean
        get() = databaseClient.isDatabaseWithCollections

    internal val databaseClient: DatabaseClient<Database, DOCUMENT>
        get() = configuration.databaseVendor.getClient(project) as DatabaseClient<Database, DOCUMENT>
}
