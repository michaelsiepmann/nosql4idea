package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.commons.model.Folder
import org.codinjutsu.tools.nosql.commons.model.Database

internal class ElasticsearchDatabase(name: String, val types: Collection<ElasticsearchCollection> = emptyList()) : Database(name), Folder<ElasticsearchCollection> {
    override fun getChildFolders() = types
}