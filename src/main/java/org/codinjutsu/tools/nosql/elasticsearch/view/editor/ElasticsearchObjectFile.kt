package org.codinjutsu.tools.nosql.elasticsearch.view.editor

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase

internal class ElasticsearchObjectFile(project: Project, configuration: ServerConfiguration, val database: ElasticsearchDatabase, val collection: ElasticsearchCollection?) :
        NoSqlDatabaseObjectFile(project, configuration, String.format("%s/%s/%s", configuration.label, database.name, collection?.name)) {

    override fun getFileType() = ElasticsearchFakeFileType
}