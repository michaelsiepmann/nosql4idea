package org.codinjutsu.tools.nosql.solr.view

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.DatabaseContext
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings
import org.codinjutsu.tools.nosql.solr.logic.SolrClient
import org.codinjutsu.tools.nosql.solr.model.SolrDatabase

internal class SolrContext(solrClient: SolrClient, serverConfiguration: ServerConfiguration, val solrDatabase: SolrDatabase) :
        DatabaseContext<SolrClient>(solrClient, serverConfiguration) {

    override fun getImportPanelSettings() =
            object : ImportPanelSettings {
                override fun getExtensions() = arrayOf(".csv", ".json", ".xml")
            }
}