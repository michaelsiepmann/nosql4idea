package org.codinjutsu.tools.nosql.commons.view.panel

import javax.swing.tree.TreeNode

internal interface TreePreparator {
    fun prepare(treeNode: TreeNode): TreeNode

    companion object {
        val NOOP: TreePreparator = object : TreePreparator {
            override fun prepare(treeNode: TreeNode) = treeNode
        }
    }
}