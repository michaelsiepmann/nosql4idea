package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

abstract class FolderDatabaseServerFolder<DOCUMENT, DATABASE : Database> constructor(databaseserver: DatabaseServer, private val project: Project) :
        DatabaseServerFolder<DATABASE>(databaseserver) {

    override val isDatabaseWithCollections: Boolean
        get() = databaseClient.isDatabaseWithCollections

    internal val databaseClient
        get() = configuration.databaseVendor.getClient(project) as DatabaseClient<DOCUMENT>
}
