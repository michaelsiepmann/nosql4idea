package org.codinjutsu.tools.nosql.elasticsearch.view.editor

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType

internal class ElasticsearchObjectFile(project: Project, configuration: ServerConfiguration, val database: ElasticsearchDatabase, val type: ElasticsearchType?) :
        NoSqlDatabaseObjectFile(project, configuration, String.format("%s/%s/%s", configuration.label, database.name, type?.name)) {

    override fun getFileType() = ElasticsearchFakeFileType
}