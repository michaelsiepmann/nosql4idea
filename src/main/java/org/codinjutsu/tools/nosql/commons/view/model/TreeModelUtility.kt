package org.codinjutsu.tools.nosql.commons.view.model

import org.apache.commons.lang.StringUtils
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.ResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.ValueDescriptor

fun findObjectIdNode(treeNode: NoSqlTreeNode): NoSqlTreeNode? {
    val descriptor = treeNode.descriptor
    if (descriptor is ResultDescriptor) { //defensive prog?
        return null
    }

    if (descriptor is AbstractKeyValueDescriptor) {
        if (StringUtils.equals(descriptor.key, "_id")) {
            return treeNode
        }
    }

    val parentTreeNode = treeNode.parent as NoSqlTreeNode
    if (parentTreeNode.descriptor is ValueDescriptor) {
        if ((parentTreeNode.parent as NoSqlTreeNode).descriptor is ResultDescriptor) {
            //find
        }
    }

    return null
}

fun findDocument(startingNode: NoSqlTreeNode): Any? {
    if (startingNode.descriptor is ValueDescriptor) {
        if ((startingNode.parent as NoSqlTreeNode).descriptor is ResultDescriptor) {
            return startingNode.descriptor.value
        }
    }
    return null
}
