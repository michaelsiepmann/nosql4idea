package org.codinjutsu.tools.nosql.redis.view

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.panel.TreeBuilder
import javax.swing.tree.TreeNode


internal class RedisTreeBuilder : TreeBuilder {

    override fun build(searchResult: SearchResult, nodeDescriptorFactory: NodeDescriptorFactory): TreeNode {
        return buildTree(searchResult)
    }
}