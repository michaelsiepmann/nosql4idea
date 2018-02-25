package org.codinjutsu.tools.nosql.elasticsearch.model.explorer

import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.CommonLeafFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.DatabaseServerFolder
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.ELASTICSEARCH_INDEX
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.ELASTICSEARCH_TYPE
import org.codinjutsu.tools.nosql.elasticsearch.configuration.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchObjectFile
import javax.swing.JOptionPane

internal class ElasticsearchIndexFolder(override val data: ElasticsearchDatabase, override val parent: DatabaseServerFolder<ElasticsearchDatabase>) : DatabaseFolder<ElasticsearchDatabase>(data) {
    override val children: Collection<Folder<*, ElasticsearchDatabase>>
        get() = data.getTypes().map { createChildFolder(it) }

    private fun createChildFolder(type: ElasticsearchType): CommonLeafFolder<ElasticsearchType, ElasticsearchDatabase> {
        return CommonLeafFolder(
                type,
                this,
                {
                    it == ELASTICSEARCH_TYPE && (databaseServer.configuration as ElasticsearchServerConfiguration).version >= ElasticsearchVersion.VERSION_20
                }
        ) {
            ElasticsearchObjectFile(it, databaseServer.configuration, data, type)
        }
    }

    override val databaseServer: DatabaseServer
        get() = parent.databaseServer

    override val database: ElasticsearchDatabase?
        get() = data

    override fun canBeDeleted(folderType: FolderType) = folderType == ELASTICSEARCH_INDEX

    override fun canCreateChild(folderType: FolderType) = folderType == ELASTICSEARCH_TYPE

    override fun createChild(): Folder<*, ElasticsearchDatabase>? {
        val typeName = JOptionPane.showInputDialog("Please enter a type-name") ?: return null
        if (typeName.isNotEmpty()) {
            val type = (parent as ElasticsearchDatabaseServerFolder).databaseClient.createFolder(databaseServer.configuration, data.name, typeName)
            if (type is ElasticsearchType) {
                database?.addType(type)
                return createChildFolder(type)
            }
        }
        return null
    }

    override fun deleteChild(child: Folder<*, ElasticsearchDatabase>) {
        (parent as ElasticsearchDatabaseServerFolder).databaseClient.dropFolder(databaseServer.configuration, (child as CommonLeafFolder).data)
    }
}