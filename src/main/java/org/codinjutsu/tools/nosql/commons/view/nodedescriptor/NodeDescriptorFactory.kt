package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor

internal interface NodeDescriptorFactory {

    fun createResultDescriptor(searchResult: SearchResult): NodeDescriptor

    fun createKeyValueDescriptor(key: String, value: Any?): NodeDescriptor

    fun createValueDescriptor(index: Int, value: Any): NodeDescriptor
}