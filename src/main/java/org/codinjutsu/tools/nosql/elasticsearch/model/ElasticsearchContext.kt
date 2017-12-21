package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient

internal class ElasticsearchContext(client: ElasticsearchClient,
                                    serverConfiguration: ServerConfiguration,
                                    val type: ElasticsearchType?,
                                    val database: ElasticsearchDatabase) :
        DatabaseContext<ElasticsearchClient>(client, serverConfiguration) {

    override fun getImportPanelSettings() =
            object : ImportPanelSettings {
                override fun getExtensions() = arrayOf(".json")
            }
}