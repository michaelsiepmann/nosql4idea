package org.codinjutsu.tools.nosql.commons.view.panel

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.buildTree

internal class DefaultTreeBuilder : TreeBuilder {
    override fun build(searchResult: SearchResult, nodeDescriptorFactory: NodeDescriptorFactory) =
            buildTree(searchResult, nodeDescriptorFactory)
}