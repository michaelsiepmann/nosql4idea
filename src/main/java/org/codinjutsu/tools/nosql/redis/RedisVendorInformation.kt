package org.codinjutsu.tools.nosql.redis

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendorInformation
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.DataType.STRING
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.redis.logic.RedisClient
import org.codinjutsu.tools.nosql.redis.model.explorer.RedisDatabaseServerFolder
import org.codinjutsu.tools.nosql.redis.view.console.RedisConsoleRunner

internal object RedisVendorInformation : DatabaseVendorInformation(true) {

    override fun availableDataTypes() = arrayOf(STRING)

    override fun createConsoleRunner(project: Project, configuration: ServerConfiguration, database: Database) =
            RedisConsoleRunner(project, configuration, database)

    override fun createDatabaseServerFolder(databaseServer: DatabaseServer, project: Project) =
            RedisDatabaseServerFolder(databaseServer)

    override fun getDatabaseUIClass() = RedisUI::class.java

    override fun getDatabaseClientClass() = RedisClient::class.java


}