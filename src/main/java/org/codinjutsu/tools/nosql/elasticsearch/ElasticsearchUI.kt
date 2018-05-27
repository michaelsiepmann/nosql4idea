package org.codinjutsu.tools.nosql.elasticsearch

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.DatabaseUI
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import org.codinjutsu.tools.nosql.commons.view.authentication.NoAuthenticationView
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchPanel
import org.codinjutsu.tools.nosql.elasticsearch.view.editor.ElasticsearchObjectFile

internal class ElasticsearchUI : DatabaseUI {

    override fun createAuthenticationView() = NoAuthenticationView()

    override fun createResultPanel(project: Project, objectFile: NoSqlDatabaseObjectFile): DatabasePanel {
        val elasticSearchObjectFile = objectFile as ElasticsearchObjectFile
        return ElasticsearchPanel(project, createContext(project, elasticSearchObjectFile))
    }

    override fun hasOptionsPanel() = false

    private fun createContext(project: Project, elasticSearchObjectFile: ElasticsearchObjectFile) =
            ElasticsearchContext.create(project, elasticSearchObjectFile.configuration, elasticSearchObjectFile.type, elasticSearchObjectFile.database)
}