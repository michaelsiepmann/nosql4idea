package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.ui.ColoredTreeCellRenderer

interface TreeCellItem {

    fun updateTreeCell(renderer : ColoredTreeCellRenderer) {}
}