package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings

abstract class AbstractDatabaseContext internal constructor(
        override val client: DatabaseClient,
        override val serverConfiguration: ServerConfiguration
) : DatabaseContext {
    override fun getImportPanelSettings(): ImportPanelSettings? = null
}