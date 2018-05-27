package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile

interface Folder<out T, DATABASE : Database> : TreeCellItem {

    val name: String?
        get() = null

    val data: T

    val parent: Folder<*, *>?
        get() = null

    val children: Collection<Folder<*, DATABASE>>

    val databaseServer: DatabaseServer

    val database: DATABASE?
        get() = null

    fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile? = null

    fun canCreateChild(folderType: FolderType): Boolean = false

    fun canBeDeleted(folderType: FolderType): Boolean = false

    fun createChild(): Folder<*, DATABASE>? = null

    fun deleteChild(child: Folder<*, *>) {}

    fun isViewableContent(): Boolean = false

    fun canShowConsoleApplication(): Boolean = false
}
