package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile

internal class CommonDatabaseFolder<DATABASE : Database>(
        override val database: DATABASE,
        override val parent: DatabaseServerFolder<DATABASE>,
        private val viewableContent: Boolean,
        private val objectFileFactory: ((Project) -> NoSqlDatabaseObjectFile))
    : DatabaseFolder<DATABASE>(database) {
    override val data: DATABASE
        get() = database

    override val databaseServer: DatabaseServer
        get() = parent.databaseServer

    override fun createNoSqlObjectFile(project: Project) =
            objectFileFactory(project)

    override fun isViewableContent() = viewableContent

    override fun canShowConsoleApplication() = parent.canShowConsoleApplication()
}
