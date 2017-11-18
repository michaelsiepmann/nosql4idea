package org.codinjutsu.tools.nosql.commons.view.columninfo

import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode

internal interface WriteableColumnInfoDecider {
    fun isNodeWriteable(treeNode: NoSqlTreeNode) : Boolean
}