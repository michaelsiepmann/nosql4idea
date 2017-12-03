package org.codinjutsu.tools.nosql.elasticsearch.model.explorer

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.explorer.Folder
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType
import org.codinjutsu.tools.nosql.commons.model.explorer.FolderType.ELASTICSEARCH_TYPE
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchObjectFile

internal class ElasticsearchCollectionFolder(override val data: ElasticsearchType, override val parent: ElasticsearchDatabaseFolder) : Folder<ElasticsearchType> {

    override val name: String?
        get() = data.name

    override val children: Collection<Folder<*>>
        get() = emptyList()

    override val databaseServer: DatabaseServer<ElasticsearchServerConfiguration>
        get() = parent.databaseServer

    override val database: ElasticsearchDatabase?
        get() = parent.database

    override fun createNoSqlObjectFile(project: Project) =
            ElasticsearchObjectFile(project, ElasticsearchServerConfiguration(ElasticsearchVersion.VERSION_20, databaseServer.configuration), parent.data, data)

    override fun canBeDeleted(folderType: FolderType) = folderType == ELASTICSEARCH_TYPE && databaseServer.configuration.version >= ElasticsearchVersion.VERSION_20

    override fun isViewableContent() = true
}