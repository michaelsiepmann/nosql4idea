package org.codinjutsu.tools.nosql.mongo.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.CommonLeafFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.MONGO_COLLECTION
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.MONGO_DATABASE
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoObjectFile
import javax.swing.JOptionPane

internal class MongoDatabaseFolder(override val data: MongoDatabase, override val parent: MongoDatabaseServerFolder) : DatabaseFolder<MongoDatabase>(data) {

    override val children: Collection<Folder<*, MongoDatabase>>
        get() = data.getCollections().map { createChildFolder(it) }

    private fun createChildFolder(type: MongoCollection): CommonLeafFolder<MongoCollection, MongoDatabase> {
        return CommonLeafFolder(
                type,
                this,
                {
                    it == MONGO_DATABASE
                }
        ) {
            MongoObjectFile(it, databaseServer.configuration, type)
        }
    }

    override val databaseServer: DatabaseServer
        get() = parent.databaseServer

    override val database: MongoDatabase?
        get() = data

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile? = null

    override fun canBeDeleted(folderType: FolderType) = folderType == MONGO_DATABASE

    override fun canCreateChild(folderType: FolderType) = folderType == MONGO_COLLECTION

    override fun createChild(): Folder<*, MongoDatabase>? {
        val collectionName = JOptionPane.showInputDialog("Please enter a collection-name")
        if (collectionName?.isNotEmpty() == true) {
            val collection = parent.databaseClient.createFolder(databaseServer.configuration, data.name, collectionName)
            if (collection is MongoCollection) {
                data.addCollection(collection)
                return createChildFolder(collection)
            }
        }
        return null
    }

    override fun deleteChild(child: Folder<*, *>) {
        parent.databaseClient.dropFolder(databaseServer.configuration, (child as CommonLeafFolder).data)
    }

    override fun canShowConsoleApplication() = parent.canShowConsoleApplication()
}