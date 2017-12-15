package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

interface ServerConfiguration {
    var label: String?
    var databaseVendor: DatabaseVendor
    var serverUrl: String?
    var userDatabase: String?
    var isConnectOnIdeStartup: Boolean
    var authenticationSettings: AuthenticationSettings
    val isSingleServer: Boolean

    fun copy() : ServerConfiguration
}