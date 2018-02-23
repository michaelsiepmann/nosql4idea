package org.codinjutsu.tools.nosql.solr

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.DatabaseUI
import org.codinjutsu.tools.nosql.commons.view.NoSqlResultView
import org.codinjutsu.tools.nosql.commons.view.authentication.DefaultAuthenticationPanel
import org.codinjutsu.tools.nosql.commons.view.editor.NoSqlDatabaseObjectFile
import org.codinjutsu.tools.nosql.solr.model.SolrContext
import org.codinjutsu.tools.nosql.solr.view.SolrPanel
import org.codinjutsu.tools.nosql.solr.view.editor.SolrObjectFile

class SolrUI : DatabaseUI {

    override fun createAuthenticationView() = DefaultAuthenticationPanel()

    override fun createResultPanel(project: Project, objectFile: NoSqlDatabaseObjectFile): NoSqlResultView {
        val solrObjectFile = objectFile as SolrObjectFile
        return SolrPanel(project, SolrContext.create(project, objectFile.configuration, solrObjectFile.solrDatabase))
    }
}
