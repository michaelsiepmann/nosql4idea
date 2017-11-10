package org.codinjutsu.tools.nosql.elasticsearch.view

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchKeyValueDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchResultDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchValueDescriptor

internal class ElasticsearchTreeModelFactory : NodeDescriptorFactory {

    override fun createResultDescriptor(searchResult: SearchResult) =
            ElasticsearchResultDescriptor(searchResult.name)

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            ElasticsearchKeyValueDescriptor.createDescriptor(key, value)

    override fun createValueDescriptor(index: Int, value: Any) =
            ElasticsearchValueDescriptor.createDescriptor(index, value)
}