package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile

interface Folder<out T> {

    val name: String?
        get() = null

    val data: T

    val parent: Folder<*>?
        get() = null

    val children: Collection<Folder<*>>

    val databaseServer: DatabaseServer<*>

    val database: Database?
        get() = null

    fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile<*>?

    fun canCreateChild(folderType: FolderType): Boolean = false

    fun canBeDeleted(folderType: FolderType): Boolean = false

    fun createChild(): Folder<*>? = null

    fun deleteChild(child: Folder<*>) {}

    fun isViewableContent(): Boolean = false

    fun canShowConsoleApplication(): Boolean = false
}
