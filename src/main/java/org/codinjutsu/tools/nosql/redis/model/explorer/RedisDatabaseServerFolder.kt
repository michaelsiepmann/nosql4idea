package org.codinjutsu.tools.nosql.redis.model.explorer

import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.CommonDatabaseFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase
import org.codinjutsu.tools.nosql.redis.view.editor.RedisObjectFile

internal class RedisDatabaseServerFolder(databaseServer: DatabaseServer)
    : DatabaseServerFolder<RedisDatabase>(databaseServer) {

    override fun createDatabaseFolder(database: Database) =
            CommonDatabaseFolder(database as RedisDatabase, this, true) {
                RedisObjectFile(it, databaseServer.configuration, database)
            }

    override fun canShowConsoleApplication() = true
}