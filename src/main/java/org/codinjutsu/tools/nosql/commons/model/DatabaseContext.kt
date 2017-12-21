package org.codinjutsu.tools.nosql.commons.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings

open class DatabaseContext<out CLIENT> internal constructor(
        val client: CLIENT,
        val serverConfiguration: ServerConfiguration
) {
    open fun getImportPanelSettings(): ImportPanelSettings? = null
}