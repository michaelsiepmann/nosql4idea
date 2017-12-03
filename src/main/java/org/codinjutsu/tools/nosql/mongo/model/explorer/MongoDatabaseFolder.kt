package org.codinjutsu.tools.nosql.mongo.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.MONGO_COLLECTION
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.MONGO_DATABASE
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase
import javax.swing.JOptionPane

internal class MongoDatabaseFolder(override val data: MongoDatabase, override val parent: MongoDatabaseServerFolder) : DatabaseFolder(data) {

    override val children: Collection<Folder<*>>
        get() = data.getCollections().map { MongoCollectionFolder(it, this) }

    override val databaseServer: DatabaseServer<*>
        get() = parent.data

    override val database: MongoDatabase?
        get() = data

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile<*>? = null

    override fun canBeDeleted(folderType: FolderType) = folderType == MONGO_DATABASE

    override fun canCreateChild(folderType: FolderType) = folderType == MONGO_COLLECTION

    override fun createChild(): Folder<*>? {
        val collectionName = JOptionPane.showInputDialog("Please enter a collection-name")
        if (collectionName?.isNotEmpty() == true) {
            val collection = parent.databaseClient.createFolder(databaseServer.configuration, data.name, collectionName)
            if (collection != null) {
                data.addCollection(collection)
                return MongoCollectionFolder(collection, this)
            }
        }
        return null
    }

    override fun deleteChild(child: Folder<*>) {
        parent.databaseClient.dropFolder(databaseServer.configuration, (child as MongoCollectionFolder).data)
    }

    override fun canShowConsoleApplication() = parent.canShowConsoleApplication()
}