package org.codinjutsu.tools.nosql.commons.view

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportPanelSettings

open class DatabaseContext<out CLIENT, out SERVERCONFIGURATION : ServerConfiguration> internal constructor(
        val client: CLIENT,
        val serverConfiguration: SERVERCONFIGURATION
) {
    open fun getImportPanelSettings(): ImportPanelSettings? = null
}