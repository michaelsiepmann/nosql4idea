package org.codinjutsu.tools.nosql.elasticsearch.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.ELASTICSEARCH_INDEX
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.ELASTICSEARCH_TYPE
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration
import javax.swing.JOptionPane

internal class ElasticsearchDatabaseFolder(override val data: ElasticsearchDatabase, override val parent: ElasticsearchDatabaseServerFolder) : DatabaseFolder(data) {
    override val children: Collection<Folder<*>>
        get() = data.getTypes().map { ElasticsearchCollectionFolder(it, this) }

    override val databaseServer: DatabaseServer<ElasticsearchServerConfiguration>
        get() = parent.data

    override val database: ElasticsearchDatabase?
        get() = data

    override fun createNoSqlObjectFile(project: Project): NoSqlDatabaseObjectFile<*>? = null

    override fun canBeDeleted(folderType: FolderType) = folderType == ELASTICSEARCH_INDEX

    override fun canCreateChild(folderType: FolderType) = folderType == ELASTICSEARCH_TYPE

    override fun createChild(): Folder<*>? {
        val typeName = JOptionPane.showInputDialog("Please enter a type-name")
        if (typeName?.isNotEmpty() == true) {
            val collection = parent.databaseClient.createFolder(databaseServer.configuration, data.name, typeName)
            if (collection != null) {
                database?.addType(collection)
                return ElasticsearchCollectionFolder(collection, this)
            }
        }
        return null
    }

    override fun deleteChild(child: Folder<*>) {
        parent.databaseClient.dropFolder(databaseServer.configuration as ElasticsearchServerConfiguration, (child as ElasticsearchCollectionFolder).data)
    }
}