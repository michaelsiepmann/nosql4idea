package org.codinjutsu.tools.nosql.commons.view.explorer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

internal class ExplorerTreeTest {

    @Test
    fun `root model is null`() {
        val tree = ExplorerTree()
        tree.model = null
        assertNull(tree.rootNode)
    }

    @Test
    fun `root model is not null`() {
        val tree = ExplorerTree()
        val node = DefaultMutableTreeNode()
        tree.model = DefaultTreeModel(node)
        assertEquals(node, tree.rootNode)
    }
}