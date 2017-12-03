package org.codinjutsu.tools.nosql.redis.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase

internal class RedisDatabaseServerFolder(databaseServer: DatabaseServer<ServerConfiguration>)
    : DatabaseServerFolder<ServerConfiguration>(databaseServer) {

    override fun createDatabaseFolder(database: Database) =
            RedisDatabaseFolder(database as RedisDatabase, this)

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile<*>? = null

    override fun canShowConsoleApplication() = true
}