package org.codinjutsu.tools.nosql.commons.history

import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.NoSqlTreeRenderer
import org.codinjutsu.tools.nosql.commons.model.explorer.TreeCellItem

internal data class HistoryItem @JvmOverloads constructor(
        var filter: String = "",
        var pinned: Boolean = false
) : TreeCellItem {
    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            append(filter)
            icon = if (pinned) {
                NoSqlTreeRenderer.ICON_PINNED
            } else {
                null
            }
        }
    }

    fun isNotEmpty() = filter.isNotEmpty()
}