package org.codinjutsu.tools.nosql.commons.view.panel

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import javax.swing.tree.TreeNode

internal interface TreeBuilder {
    fun build(searchResult: SearchResult, nodeDescriptorFactory: NodeDescriptorFactory): TreeNode
}