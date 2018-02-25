package org.codinjutsu.tools.nosql.commons.view.columninfo

import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode

internal interface WriteableColumnInfoDecider {
    fun isNodeWriteable(treeNode: NoSqlTreeNode) : Boolean

    companion object {
        val DONT_WRITE = object : WriteableColumnInfoDecider {
            override fun isNodeWriteable(treeNode: NoSqlTreeNode) = false
        }
    }
}