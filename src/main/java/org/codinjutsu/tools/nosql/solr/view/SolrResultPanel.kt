package org.codinjutsu.tools.nosql.solr.view

import com.intellij.openapi.project.Project
import org.apache.solr.common.SolrDocument
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel
import org.codinjutsu.tools.nosql.solr.model.SolrResult

internal class SolrResultPanel(project: Project, operations: NoSQLResultPanelDocumentOperations<SolrDocument>) :
        AbstractNoSQLResultPanel<SolrResult, SolrDocument>(project, operations, SolrTreeModelFactory()) {

    override fun createEditionPanel() = null
}