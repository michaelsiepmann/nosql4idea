package org.codinjutsu.tools.nosql.mongo

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.DatabaseVendorInformation
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase
import org.codinjutsu.tools.nosql.mongo.model.explorer.MongoDatabaseServerFolder
import org.codinjutsu.tools.nosql.mongo.view.console.MongoConsoleRunner

internal object MongoVendorInformation : DatabaseVendorInformation(true) {

    override fun availableDataTypes() = DataType.values()

    override fun createConsoleRunner(project: Project, configuration: ServerConfiguration, database: Database) =
            MongoConsoleRunner(project, configuration, database as MongoDatabase)

    override fun createDatabaseServerFolder(databaseServer: DatabaseServer, project: Project) =
            MongoDatabaseServerFolder(databaseServer, project)

    override fun getDatabaseUIClass() = MongoUI::class.java

    override fun getDatabaseClientClass() = MongoClient::class.java
}