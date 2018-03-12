package org.codinjutsu.tools.nosql.elasticsearch.model

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AbstractDatabaseContext
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient

internal class ElasticsearchContext(client: ElasticsearchClient,
                                    serverConfiguration: ServerConfiguration,
                                    val type: ElasticsearchType?,
                                    val database: ElasticsearchDatabase) :
        AbstractDatabaseContext(client, serverConfiguration) {

    override fun getImportPanelSettings() =
            object : ImportPanelSettings {
                override fun getExtensions() = arrayOf(".json")
            }

    companion object {
        internal fun create(project: Project, serverConfiguration: ServerConfiguration, type: ElasticsearchType?, database: ElasticsearchDatabase) =
                ElasticsearchContext(ElasticsearchClient.getInstance(project), serverConfiguration, type, database)

    }
}