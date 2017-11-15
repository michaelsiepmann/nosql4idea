package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal interface NodeDescriptorFactory<out DOCUMENT> {

    fun createResultDescriptor(name: String): NodeDescriptor

    fun createKeyValueDescriptor(key: String, value: Any?): NodeDescriptor

    fun createValueDescriptor(index: Int, value: Any): NodeDescriptor

    fun processObject(parentNode: NoSqlTreeNode, value: Any?)

    fun buildDBObject(rootNode: NoSqlTreeNode): DOCUMENT

    fun isArray(value: Any?): Boolean

    fun toArray(value: Any): Iterator<Any>

    fun isObject(value: Any?): Boolean

    fun createObjectWrapper(value: Any?): ObjectWrapper
}