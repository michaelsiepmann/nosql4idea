package org.codinjutsu.tools.nosql.mongo.view.model

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper
import org.codinjutsu.tools.nosql.mongo.model.MongoResult
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoKeyValueDescriptor
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoResultDescriptor
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoValueDescriptor
import javax.swing.tree.TreeNode

internal class MongoTreeModelFactory : NodeDescriptorFactory<DBObject> {
    override fun createResultDescriptor(name: String) = MongoResultDescriptor()

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            MongoKeyValueDescriptor.createDescriptor(key, value)

    override fun createValueDescriptor(index: Int, value: Any) =
            MongoValueDescriptor.createDescriptor(index, value)

    // todo
    fun buildJsonTree(mongoResult: MongoResult): TreeNode {
        val rootNode = NoSqlTreeNode(MongoResultDescriptor(mongoResult.name))

        val mongoObjects = mongoResult.mongoObjects
        var i = 0
        for (mongoObject in mongoObjects) {
            if (mongoObject is BasicDBList) {
                processDbObject(rootNode, mongoObject)
            } else if (mongoObject is BasicDBObject) {//dead code?
                val currentNode = NoSqlTreeNode(MongoValueDescriptor.createDescriptor(i++, mongoObject))
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

    private fun processDbObject(parentNode: NoSqlTreeNode, mongoObject: DBObject) {
        if (mongoObject is BasicDBList) {
            for (i in mongoObject.indices) {
                val mongoObjectOfList = mongoObject[i]
                val currentNode = NoSqlTreeNode(MongoValueDescriptor.createDescriptor(i, mongoObjectOfList))
                if (mongoObjectOfList is DBObject) {
                    processDbObject(currentNode, mongoObjectOfList)
                }
                parentNode.add(currentNode)
            }
        } else if (mongoObject is BasicDBObject) {
            for (key in mongoObject.keys) {
                val value = mongoObject.get(key)
                val currentNode = NoSqlTreeNode(MongoKeyValueDescriptor.createDescriptor(key, value))
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
            val descriptor = node.descriptor as MongoKeyValueDescriptor
            val value = descriptor.value
            if (value is DBObject) {
                if (value is BasicDBList) {
                    basicDBObject.put(descriptor.key, buildDBList(node))
                } else {
                    basicDBObject.put(descriptor.key, buildDBObject(node))
                }
            } else {
                basicDBObject.put(descriptor.key, value)
            }
        }

        return basicDBObject
    }

    private fun buildDBList(parentNode: NoSqlTreeNode): DBObject {
        val basicDBList = BasicDBList()
        val children = parentNode.children()
        while (children.hasMoreElements()) {
            val node = children.nextElement() as NoSqlTreeNode
            val descriptor = node.descriptor as MongoValueDescriptor
            val value = descriptor.value
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

    override fun createObjectWrapper(value: Any?): ObjectWrapper {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
