package org.codinjutsu.tools.nosql.elasticsearch.view

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchKeyValueDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchResultDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchValueDescriptor

internal class ElasticsearchTreeModelFactory : NodeDescriptorFactory<JsonObject> {

    override fun createResultDescriptor(searchResult: SearchResult) =
            ElasticsearchResultDescriptor(searchResult.name)

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            ElasticsearchKeyValueDescriptor.createDescriptor(key, value)

    override fun createValueDescriptor(index: Int, value: Any) =
            ElasticsearchValueDescriptor.createDescriptor(index, value)

    override fun processObject(parentNode: NoSqlTreeNode, value: Any?) {
        if (value is JsonObject) {
            processDbObject(parentNode, value)
        }
    }

    private fun processDbObject(parentNode: NoSqlTreeNode, mongoObject: JsonObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildDBObject(rootNode: NoSqlTreeNode): JsonObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}