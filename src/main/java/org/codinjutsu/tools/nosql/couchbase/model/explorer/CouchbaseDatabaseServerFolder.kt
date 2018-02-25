package org.codinjutsu.tools.nosql.couchbase.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.CommonDatabaseFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.couchbase.view.editor.CouchbaseObjectFile

internal class CouchbaseDatabaseServerFolder(databaseServer: DatabaseServer) :
        DatabaseServerFolder<Database>(databaseServer) {

    override fun createDatabaseFolder(database: Database) =
            CommonDatabaseFolder(database, this, true) {
                CouchbaseObjectFile(it, databaseServer.configuration, database)
            }

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile? = null
}