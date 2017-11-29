package org.codinjutsu.tools.nosql.elasticsearch.view

import org.codinjutsu.tools.nosql.commons.view.DatabaseContext
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchServerConfiguration

internal class ElasticsearchContext(client: ElasticsearchClient,
                                    serverConfiguration: ElasticsearchServerConfiguration,
                                    val collection: ElasticsearchCollection?,
                                    val database: ElasticsearchDatabase) :
        DatabaseContext<ElasticsearchClient, ElasticsearchServerConfiguration>(client, serverConfiguration)