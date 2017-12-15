package org.codinjutsu.tools.nosql.mongo.model.explorer

import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.NoSqlTreeRenderer
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.MONGO_COLLECTION
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoObjectFile

internal class MongoCollectionFolder(override val data: MongoCollection, override val parent: MongoDatabaseFolder) : Folder<MongoCollection> {
    override val name: String?
        get() = data.name

    override val children: Collection<Folder<*>>
        get() = emptyList()

    override val databaseServer: DatabaseServer
        get() = parent.databaseServer

    override val database: MongoDatabase?
        get() = parent.database

    override fun createNoSqlObjectFile(project: Project) =
            MongoObjectFile(project, databaseServer.configuration, data)

    override fun canBeDeleted(folderType: FolderType) = folderType == MONGO_COLLECTION

    override fun isViewableContent() = true

    override fun canShowConsoleApplication() = parent.canShowConsoleApplication()

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            append(data.name)
            icon = NoSqlTreeRenderer.MONGO_COLLECTION
        }
    }
}
