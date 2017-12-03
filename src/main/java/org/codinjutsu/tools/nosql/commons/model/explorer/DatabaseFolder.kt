package org.codinjutsu.tools.nosql.commons.model.explorer

import org.codinjutsu.tools.nosql.commons.model.Database

internal abstract class DatabaseFolder(override val data: Database) : Folder<Database> {

    override val name: String?
        get() = data.name

    override val children: Collection<Folder<*>>
        get() = emptyList()
}