package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.CollectableDatabase
import org.codinjutsu.tools.nosql.commons.model.Database

internal class ElasticsearchDatabase(name: String, val types: Collection<ElasticsearchCollection> = emptyList()) : Database(name), CollectableDatabase<ElasticsearchCollection> {
    override fun getCollections() = types
}