package org.codinjutsu.tools.nosql.commons.model.explorer

import com.intellij.ui.ColoredTreeCellRenderer
import org.codinjutsu.tools.nosql.NoSqlTreeRenderer.DATABASE
import org.codinjutsu.tools.nosql.commons.model.Database

internal abstract class DatabaseFolder(override val data: Database) : Folder<Database> {

    override val name: String?
        get() = data.name

    override val children: Collection<Folder<*>>
        get() = emptyList()

    override fun updateTreeCell(renderer: ColoredTreeCellRenderer) {
        renderer.apply {
            append(data.name)
            icon = DATABASE
        }
    }
}