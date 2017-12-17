package org.codinjutsu.tools.nosql.solr.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.solr.model.SolrDatabase

internal class SolrDatabaseServerFolder(databaseServer: DatabaseServer) :
        DatabaseServerFolder(databaseServer) {
    override fun createDatabaseFolder(database: Database) =
            SolrDatabaseFolder(database as SolrDatabase, this)

    override fun createNoSqlObjectFile(project: Project) = null
}