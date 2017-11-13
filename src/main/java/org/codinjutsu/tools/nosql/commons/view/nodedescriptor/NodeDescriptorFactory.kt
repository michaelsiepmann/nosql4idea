package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode

internal interface NodeDescriptorFactory<out DOCUMENT> {

    fun createResultDescriptor(searchResult: SearchResult): NodeDescriptor

    fun createKeyValueDescriptor(key: String, value: Any?): NodeDescriptor

    fun createValueDescriptor(index: Int, value: Any): NodeDescriptor

    fun processObject(parentNode: NoSqlTreeNode, value: Any?)

    fun buildDBObject(rootNode: NoSqlTreeNode): DOCUMENT
}