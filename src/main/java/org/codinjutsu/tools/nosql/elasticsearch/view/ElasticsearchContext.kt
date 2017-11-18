package org.codinjutsu.tools.nosql.elasticsearch.view

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.DatabaseContext
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase

internal class ElasticsearchContext(client: ElasticsearchClient,
                                    serverConfiguration: ServerConfiguration,
                                    val elasticsearchCollection: ElasticsearchCollection?,
                                    val elasticsearchDatabase: ElasticsearchDatabase) :
        DatabaseContext<ElasticsearchClient>(client, serverConfiguration)