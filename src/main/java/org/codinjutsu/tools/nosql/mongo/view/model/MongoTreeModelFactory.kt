package org.codinjutsu.tools.nosql.mongo.view.model

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getBooleanAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getNumberAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getObjectAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getObjectIdAttribute
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.BooleanKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DateKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NumberKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardDateValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardNullValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardStringValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper
import org.codinjutsu.tools.nosql.mongo.model.MongoSearchResult
import org.codinjutsu.tools.nosql.mongo.view.wrapper.MongoObjectWrapper
import java.util.*
import javax.swing.tree.TreeNode

class MongoTreeModelFactory : NodeDescriptorFactory<DBObject> {
    override fun createResultDescriptor(name: String) = StandardResultDescriptor()

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            when (value) {
                null -> NullKeyValueDescriptor(key)
                is Boolean -> BooleanKeyValueDescriptor(key, value.toString().toBoolean())
                is Int -> NumberKeyValueDescriptor(key, value.toString().toInt())
                is Double -> object : TypedKeyValueDescriptor<Double>(key, value, getNumberAttribute()) {
                    override fun setValue(value: Any?) {
                        super.setValue(value.toString().toDouble())
                    }
                }
                is Long -> object : TypedKeyValueDescriptor<Long>(key, value, getNumberAttribute()) {
                    override fun setValue(value: Any?) {
                        super.setValue(value.toString().toLong())
                    }
                }
                is String -> StringKeyValueDescriptor(key, value)
                is Date -> DateKeyValueDescriptor(key, value)
                is ObjectId -> TypedKeyValueDescriptor(key, value, getObjectIdAttribute())
                else -> if (value is DBObject) {
                    TypedKeyValueDescriptor(key, value, getObjectAttribute())
                } else DefaultKeyValueDescriptor(key, value, getStringAttribute())
            }

    override fun createValueDescriptor(index: Int, value: Any): StandardValueDescriptor<*> {
        if (value == null) {
            return StandardNullValueDescriptor(index)
        }

        return when (value) {
            is String -> StandardStringValueDescriptor(index, value)
            is Boolean -> object : StandardValueDescriptor<Boolean>(index, value, getBooleanAttribute()) {
                override fun setValue(value: Any) {
                    super.setValue(value.toString().toBoolean())
                }
            }
            is Number -> object : StandardValueDescriptor<Number>(index, value, getNumberAttribute()) {
                override fun setValue(value: Any) {
                    var number = value as String
                    number = number.replace("(.*)\\..*".toRegex(), "$1")
                    super.setValue(number.toInt())
                }
            }
            is Date -> StandardDateValueDescriptor(index, value)
            is DBObject -> StandardValueDescriptor(index, value, getObjectAttribute())
            else -> StandardValueDescriptor(index, value, getStringAttribute())
        }
    }

    // todo
    fun buildJsonTree(mongoSearchResult: MongoSearchResult): TreeNode {
        val rootNode = NoSqlTreeNode(StandardResultDescriptor(mongoSearchResult.name))

        val mongoObjects = mongoSearchResult.mongoObjects
        var i = 0
        for (mongoObject in mongoObjects) {
            if (mongoObject is BasicDBList) {
                processDbObject(rootNode, mongoObject)
            } else if (mongoObject is BasicDBObject) {//dead code?
                val currentNode = NoSqlTreeNode(createValueDescriptor(i++, mongoObject))
                processDbObject(currentNode, mongoObject)
                rootNode.add(currentNode)
            }
        }
        return rootNode
    }

    override fun processObject(parentNode: NoSqlTreeNode, value: Any?) {
        if (value is DBObject) {
            processDbObject(parentNode, value)
        }
    }

    fun processDbObject(parentNode: NoSqlTreeNode, mongoObject: DBObject) {
        if (mongoObject is BasicDBList) {
            for (i in mongoObject.indices) {
                val mongoObjectOfList = mongoObject[i]
                val currentNode = NoSqlTreeNode(createValueDescriptor(i, mongoObjectOfList))
                if (mongoObjectOfList is DBObject) {
                    processDbObject(currentNode, mongoObjectOfList)
                }
                parentNode.add(currentNode)
            }
        } else if (mongoObject is BasicDBObject) {
            for (key in mongoObject.keys) {
                val value = mongoObject.get(key)
                val currentNode = NoSqlTreeNode(createKeyValueDescriptor(key, value))
                if (value is DBObject) {
                    processDbObject(currentNode, value)
                }
                parentNode.add(currentNode)
            }
        }
    }

    override fun buildDBObject(rootNode: NoSqlTreeNode): DBObject {
        val basicDBObject = BasicDBObject()
        val children = rootNode.children()
        while (children.hasMoreElements()) {
            val node = children.nextElement() as NoSqlTreeNode
            val descriptor = node.descriptor as TypedKeyValueDescriptor
            val value = descriptor.value
            if (value is DBObject) {
                if (value is BasicDBList) {
                    basicDBObject[descriptor.key] = buildDBList(node)
                } else {
                    basicDBObject[descriptor.key] = buildDBObject(node)
                }
            } else {
                basicDBObject[descriptor.key] = value
            }
        }

        return basicDBObject
    }

    private fun buildDBList(parentNode: NoSqlTreeNode): DBObject {
        val basicDBList = BasicDBList()
        val children = parentNode.children()
        while (children.hasMoreElements()) {
            val node = children.nextElement() as NoSqlTreeNode
            val value = node.descriptor.value
            if (value is DBObject) {
                if (value is BasicDBList) {
                    basicDBList.add(buildDBList(node))
                } else {
                    basicDBList.add(buildDBObject(node))
                }
            } else {
                basicDBList.add(value)
            }
        }
        return basicDBList
    }

    override fun isArray(value: Any?) = value is BasicDBList

    override fun toArray(value: Any) = (value as BasicDBList).iterator()

    override fun isObject(value: Any?) = value is DBObject

    override fun createObjectWrapper(value: Any?): ObjectWrapper = MongoObjectWrapper(value as DBObject)
}
