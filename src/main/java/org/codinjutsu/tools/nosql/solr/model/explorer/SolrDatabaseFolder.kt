package org.codinjutsu.tools.nosql.solr.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.solr.model.SolrDatabase
import org.codinjutsu.tools.nosql.solr.view.editor.SolrObjectFile

internal class SolrDatabaseFolder(override val database: SolrDatabase, override val parent: SolrDatabaseServerFolder) : DatabaseFolder(database) {

    override val data: Database
        get() = database

    override val databaseServer: DatabaseServer
        get() = parent.data

    override fun createNoSqlObjectFile(project: Project) =
            SolrObjectFile(project, databaseServer.configuration, database)

    override fun isViewableContent() = true
}
