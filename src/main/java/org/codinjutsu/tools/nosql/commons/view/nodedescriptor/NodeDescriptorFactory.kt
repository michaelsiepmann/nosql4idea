package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.ValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal interface NodeDescriptorFactory<out DOCUMENT> {

    fun createResultDescriptor(name: String): ResultDescriptor<*>

    fun createKeyValueDescriptor(key: String, value: Any?): KeyValueDescriptor<*>

    fun createValueDescriptor(index: Int, value: Any): ValueDescriptor<*>

    fun processObject(parentNode: NoSqlTreeNode, value: Any?)

    fun buildDBObject(rootNode: NoSqlTreeNode): DOCUMENT

    fun isArray(value: Any?): Boolean

    fun toArray(value: Any): Iterator<Any>

    fun isObject(value: Any?): Boolean

    fun createObjectWrapper(value: Any?): ObjectWrapper
}