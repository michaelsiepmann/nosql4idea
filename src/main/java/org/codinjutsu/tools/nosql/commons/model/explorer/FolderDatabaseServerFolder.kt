package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.SearchResult

abstract class FolderDatabaseServerFolder<DOCUMENT, RESULT : SearchResult, DATABASE : Database> constructor(databaseserver: DatabaseServer, private val project: Project) :
        DatabaseServerFolder<DATABASE>(databaseserver) {

    override val isDatabaseWithCollections: Boolean
        get() = databaseClient.isDatabaseWithCollections

    internal val databaseClient
        get() = configuration.databaseVendor.getClient(project) as DatabaseClient<DATABASE, RESULT, DOCUMENT>
}
