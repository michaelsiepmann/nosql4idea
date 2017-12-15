package org.codinjutsu.tools.nosql.mongo.model.explorer

import com.intellij.openapi.project.Project
import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderDatabaseServerFolder
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase

internal class MongoDatabaseServerFolder(databaseserver: DatabaseServer, databaseVendorClientManager: DatabaseVendorClientManager)
    : FolderDatabaseServerFolder<DBObject>(databaseserver, databaseVendorClientManager) {
    override fun createDatabaseFolder(database: Database) =
            MongoDatabaseFolder(database as MongoDatabase, this)

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile? = null

    override fun deleteChild(child: Folder<*>) {
        val configuration = configuration
        databaseClient.dropDatabase(configuration, child.data as MongoDatabase)
    }

    override fun canShowConsoleApplication() = true
}
