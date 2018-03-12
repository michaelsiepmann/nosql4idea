package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings

internal interface DatabaseContext {
    val client: DatabaseClient
    val serverConfiguration: ServerConfiguration
    fun getImportPanelSettings(): ImportPanelSettings?

    fun getDelegatedContext() = this
}