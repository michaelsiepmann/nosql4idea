package org.codinjutsu.tools.nosql.commons.view

import org.codinjutsu.tools.nosql.ServerConfiguration

open class DatabaseContext<out CLIENT, out SERVERCONFIGURATION : ServerConfiguration>(
        val client: CLIENT,
        val serverConfiguration: SERVERCONFIGURATION
)