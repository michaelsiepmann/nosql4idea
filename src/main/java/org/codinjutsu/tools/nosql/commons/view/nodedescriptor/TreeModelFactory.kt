package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseArrayImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabaseObjectImpl
import org.codinjutsu.tools.nosql.commons.model.internal.layer.impl.DatabasePrimitiveImpl
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor
import javax.swing.tree.TreeNode

internal fun buildTree(searchResult: SearchResult, nodeDescriptorFactory: NodeDescriptorFactory): TreeNode {
    val root = NoSqlTreeNode(StandardResultDescriptor(searchResult.name))
    searchResult.records.forEach { wrapper ->
        wrapper.names().forEach { name ->
            val value = wrapper[name]
            if (value != null) {
                val current = NoSqlTreeNode(nodeDescriptorFactory.createKeyValueDescriptor(name, value))
                processDatabaseObject(current, value, nodeDescriptorFactory)
                root.add(current)
            }
        }
    }
    return root
}

private fun processDatabaseObject(parentNode: NoSqlTreeNode, databaseElement: DatabaseElement, nodeDescriptorFactory: NodeDescriptorFactory) {
    if (databaseElement is DatabaseArray) {
        databaseElement.withIndex().forEach { (index, element) ->
            val currentNode = NoSqlTreeNode(nodeDescriptorFactory.createIndexValueDescriptor(index, element))
            processDatabaseObject(currentNode, element, nodeDescriptorFactory)
            parentNode.add(currentNode)
        }
    } else if (databaseElement is DatabaseObject) {
        databaseElement.names().forEach {
            val value = databaseElement[it]
            if (value != null) {
                val currentNode = NoSqlTreeNode(nodeDescriptorFactory.createKeyValueDescriptor(it, value))
                processDatabaseObject(currentNode, value, nodeDescriptorFactory)
                parentNode.add(currentNode)
            }
        }
    }
}

internal fun processObject(parentNode: NoSqlTreeNode, value: DatabaseElement?, nodeDescriptorFactory: NodeDescriptorFactory) {
    value?.let {
        processDatabaseObject(parentNode, it, nodeDescriptorFactory)
    }
}

internal fun buildDBObject(rootNode: NoSqlTreeNode): DatabaseObject {
    val result = DatabaseObjectImpl()
    rootNode.forEach {
        val descriptor = it.descriptor
        if (descriptor is KeyValueDescriptor) {
            result.add(descriptor.key, buildValue(it, descriptor.value))
        }
    }
    return result
}

private fun buildValue(node: NoSqlTreeNode, value: Any): DatabaseElement {
    return when (value) {
        is DatabaseElement -> when {
            value.isArray() -> buildArray(node)
            value.isObject() -> buildDBObject(node)
            else -> DatabasePrimitiveImpl((value as DatabasePrimitive).value())
        }
        else -> DatabasePrimitiveImpl(value)
    }
}

private fun buildArray(node: NoSqlTreeNode): DatabaseElement {
    val result = DatabaseArrayImpl()
    node.forEach {
        result.add(buildValue(it, it.descriptor.value))
    }
    return result
}

private fun NoSqlTreeNode.forEach(handler: (NoSqlTreeNode) -> Unit) {
    children().iterator()
            .forEach {
                if (it is NoSqlTreeNode) {
                    handler(it)
                }
            }
}
