package org.codinjutsu.tools.nosql.elasticsearch.view.editor

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchType
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration

internal class ElasticsearchObjectFile(project: Project, configuration: ElasticsearchServerConfiguration, val database: ElasticsearchDatabase, val type: ElasticsearchType?) :
        NoSqlDatabaseObjectFile<ElasticsearchServerConfiguration>(project, configuration, String.format("%s/%s/%s", configuration.label, database.name, type?.name)) {

    override fun getFileType() = ElasticsearchFakeFileType
}