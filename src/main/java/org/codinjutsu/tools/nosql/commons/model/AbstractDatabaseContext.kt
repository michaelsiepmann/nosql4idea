package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings

abstract class AbstractDatabaseContext<out CLIENT : DatabaseClient<*>> internal constructor(
        override val client: CLIENT,
        override val serverConfiguration: ServerConfiguration
) : DatabaseContext {
    override fun getImportPanelSettings(): ImportPanelSettings? = null
}