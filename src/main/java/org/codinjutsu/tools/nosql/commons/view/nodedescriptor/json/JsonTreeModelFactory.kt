package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.process
import org.codinjutsu.tools.nosql.commons.model.JsonObjectObjectWrapper
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode

internal class JsonTreeModelFactory : NodeDescriptorFactory<JsonObject> {
    override fun createResultDescriptor(name: String) =
            JsonResultDescriptor(name)

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            JsonKeyValueDescriptor.createDescriptor(key, value)

    override fun createValueDescriptor(index: Int, value: Any) =
            JsonValueDescriptor.createDescriptor(index, value)

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
                    it.userObject as JsonDescriptor
                }
                .forEach {
                    it.buildObject(result)
                }
        return result
    }

    override fun isArray(value: Any?) = value is JsonArray

    override fun toArray(value: Any) = (value as JsonArray).iterator()

    override fun isObject(value: Any?) = value is JsonObject

    override fun createObjectWrapper(value: Any?) = JsonObjectObjectWrapper(value as JsonObject)
}
