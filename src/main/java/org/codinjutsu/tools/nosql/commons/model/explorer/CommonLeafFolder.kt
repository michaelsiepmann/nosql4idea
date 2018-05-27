package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.NoSqlTreeRenderer
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.NamedObject
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile

internal class CommonLeafFolder<out TYPE : NamedObject, DATABASE : Database>(
        override val data: TYPE,
        override val parent: DatabaseFolder<DATABASE>,
        private val deletablePredicate: (FolderType) -> Boolean,
        private val objectFileFactoy: (Project) -> NoSqlDatabaseObjectFile) :
        Folder<TYPE, DATABASE> {

    override val name: String?
        get() = data.name

    override val children: Collection<Folder<*, DATABASE>>
        get() = emptyList()

    override val databaseServer: DatabaseServer
        get() = parent.databaseServer

    override val database: DATABASE?
        get() = parent.database

    override fun createNoSqlObjectFile(project: Project) =
            objectFileFactoy(project)

    override fun canBeDeleted(folderType: FolderType) = deletablePredicate(folderType)

    override fun isViewableContent() = true

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            append(data.name)
            icon = NoSqlTreeRenderer.ICON_COLLECTION
        }
    }
}