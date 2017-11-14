package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal fun <DOCUMENT> buildTree(searchResult: SearchResult, nodeDescriptorFactory: NodeDescriptorFactory<DOCUMENT>): NoSqlTreeNode {
    val resultTreeNode = NoSqlTreeNode(nodeDescriptorFactory.createResultDescriptor(searchResult.name))
    searchResult.records.forEach { processRecord(resultTreeNode, it, nodeDescriptorFactory) }
    return resultTreeNode
}

private fun <DOCUMENT> processRecord(parentNode: NoSqlTreeNode, record: ObjectWrapper, nodeDescriptorFactory: NodeDescriptorFactory<DOCUMENT>) {
    record.names.forEach {
        val value = record.get(it)
        val currentNode = NoSqlTreeNode(nodeDescriptorFactory.createKeyValueDescriptor(it, value))
        process(value, currentNode, nodeDescriptorFactory)
        parentNode.add(currentNode)
    }
}

private fun <DOCUMENT> processRecordListValues(parentNode: NoSqlTreeNode, values: Any, nodeDescriptorFactory: NodeDescriptorFactory<DOCUMENT>) {
    for ((index, value) in nodeDescriptorFactory.toArray(values).withIndex()) {
        val currentValueNode = NoSqlTreeNode(nodeDescriptorFactory.createValueDescriptor(index, value))
        process(value, currentValueNode, nodeDescriptorFactory)
        parentNode.add(currentValueNode)
    }
}

private fun <DOCUMENT> process(value: Any?, currentValueNode: NoSqlTreeNode, nodeDescriptorFactory: NodeDescriptorFactory<DOCUMENT>) {
    if (value != null) {
        if (nodeDescriptorFactory.isArray(value)) {
            processRecordListValues(currentValueNode, value, nodeDescriptorFactory)
        } else if (value is ObjectWrapper) {
            processRecord(currentValueNode, value, nodeDescriptorFactory)
        }
    }
}
