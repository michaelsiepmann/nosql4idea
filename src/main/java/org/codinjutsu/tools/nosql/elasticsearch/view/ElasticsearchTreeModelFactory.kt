package org.codinjutsu.tools.nosql.elasticsearch.view

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.process
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchObjectWrapper
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchKeyValueDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchResultDescriptor
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchValueDescriptor

internal class ElasticsearchTreeModelFactory : NodeDescriptorFactory<JsonObject> {
    override fun createResultDescriptor(name: String) =
            ElasticsearchResultDescriptor(name)

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            ElasticsearchKeyValueDescriptor.createDescriptor(key, value)

    override fun createValueDescriptor(index: Int, value: Any) =
            ElasticsearchValueDescriptor.createDescriptor(index, value)

    override fun processObject(parentNode: NoSqlTreeNode, value: Any?) {
        if (value is JsonObject) {
            process(value, parentNode, this)
        }
    }

    override fun buildDBObject(rootNode: NoSqlTreeNode): JsonObject {
        val result = JsonObject()
        rootNode.childTreeNodes
                .asSequence()
                .map {
                    it.userObject as ElasticsearchDescriptor
                }
                .forEach {
                    it.buildObject(result)
                }
        return result
    }

    override fun isArray(value: Any?) = value is JsonArray

    override fun toArray(value: Any) = (value as JsonArray).iterator()

    override fun isObject(value: Any?) = value is JsonObject

    override fun createObjectWrapper(value: Any?) = ElasticsearchObjectWrapper(value as JsonObject)
}
