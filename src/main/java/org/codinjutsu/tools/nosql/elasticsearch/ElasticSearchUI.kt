package org.codinjutsu.tools.nosql.elasticsearch

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.DatabaseUI
import org.codinjutsu.tools.nosql.commons.view.NoSqlResultView
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchAuthenticationPanel
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchPanel
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchObjectFile

internal class ElasticSearchUI : DatabaseUI<ElasticsearchServerConfiguration> {

    override fun createAythenticationView() = ElasticsearchAuthenticationPanel()

    override fun createResultPanel(project: Project, objectFile: NoSqlDatabaseObjectFile<ElasticsearchServerConfiguration>): NoSqlResultView {
        val elasticSearchObjectFile = objectFile as ElasticsearchObjectFile
        return ElasticsearchPanel(project, ElasticsearchContext(ElasticsearchClient.getInstance(project), elasticSearchObjectFile.configuration, elasticSearchObjectFile.type, elasticSearchObjectFile.database))
    }
}