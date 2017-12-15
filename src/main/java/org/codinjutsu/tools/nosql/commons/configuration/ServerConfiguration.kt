package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

interface ServerConfiguration {
    val label: String?
    val databaseVendor: DatabaseVendor
    val serverUrl: String
    val userDatabase: String?
    val isConnectOnIdeStartup: Boolean
    val authenticationSettings: AuthenticationSettings
    val isSingleServer: Boolean

    fun copy() : ServerConfiguration
}