package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.Query

internal class ElasticsearchQuery(override val limit: Int, val collection: ElasticsearchCollection) : Query
