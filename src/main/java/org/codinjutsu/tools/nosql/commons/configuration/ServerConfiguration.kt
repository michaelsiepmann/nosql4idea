package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendorInformation
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

internal interface ServerConfiguration {
    val label: String?
    val databaseVendor: DatabaseVendor
    val serverUrl: String
    val userDatabase: String?
    val isConnectOnIdeStartup: Boolean
    val authenticationSettings: AuthenticationSettings
    val isSingleServer: Boolean

    val databaseVendorInformation: DatabaseVendorInformation
        get() = databaseVendor.databaseVendorInformation

    fun copy(): ServerConfiguration
}