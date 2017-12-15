package org.codinjutsu.tools.nosql.redis.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase
import org.codinjutsu.tools.nosql.redis.view.editor.RedisObjectFile

internal class RedisDatabaseFolder(override val data: RedisDatabase, override val parent: RedisDatabaseServerFolder) : DatabaseFolder(data) {

    override val databaseServer: DatabaseServer
        get() = parent.data

    override val database: RedisDatabase?
        get() = data

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile? {
        return RedisObjectFile(project, databaseServer.configuration, data)
    }

    override fun isViewableContent() = true

    override fun canShowConsoleApplication() = parent.canShowConsoleApplication()
}