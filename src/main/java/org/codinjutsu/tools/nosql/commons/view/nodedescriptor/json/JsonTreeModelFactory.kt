package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.codinjutsu.tools.nosql.commons.model.JsonObjectObjectWrapper
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.BooleanKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NumberKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.process

internal class JsonTreeModelFactory : NodeDescriptorFactory<JsonObject> {
    override fun createResultDescriptor(name: String) =
            JsonResultDescriptor(name)

    override fun createKeyValueDescriptor(key: String, value: Any?): KeyValueDescriptor<*> =
            when (value) {
                null -> JsonKeyValueDescriptor(NullKeyValueDescriptor(key))
                is JsonPrimitive -> when {
                    value.isBoolean -> JsonKeyValueDescriptor(BooleanKeyValueDescriptor(key, value.asBoolean))
                    value.isNumber -> JsonKeyValueDescriptor(NumberKeyValueDescriptor(key, value.asNumber))
                    value.isString -> JsonKeyValueDescriptor(StringKeyValueDescriptor(key, value.asString))
                    else -> JsonKeyValueDescriptor(DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value)))
                }
                is String -> JsonKeyValueDescriptor(StringKeyValueDescriptor(key, value))
                else -> JsonKeyValueDescriptor(DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value)))
            }

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
