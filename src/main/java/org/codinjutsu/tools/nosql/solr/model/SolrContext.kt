package org.codinjutsu.tools.nosql.solr.model

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AbstractDatabaseContext
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings
import org.codinjutsu.tools.nosql.solr.logic.SolrClient

internal class SolrContext(solrClient: SolrClient, serverConfiguration: ServerConfiguration, val solrDatabase: Database) :
        AbstractDatabaseContext(solrClient, serverConfiguration) {

    override fun getImportPanelSettings() =
            object : ImportPanelSettings {
                override fun getExtensions() = arrayOf(".csv", ".json", ".xml")
            }

    companion object {
        internal fun create(project: Project, serverConfiguration: ServerConfiguration, database: Database) =
                SolrContext(SolrClient.instance(project), serverConfiguration, database)
    }
}