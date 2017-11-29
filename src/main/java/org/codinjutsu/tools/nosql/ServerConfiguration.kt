package org.codinjutsu.tools.nosql

import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

interface ServerConfiguration : Cloneable {
    var label: String?
    var databaseVendor: DatabaseVendor
    var serverUrl: String?
    var userDatabase: String?
    var isConnectOnIdeStartup: Boolean
    var shellArgumentsLine: String?
    var shellWorkingDir: String?
    var authenticationSettings: AuthenticationSettings
    val isSingleServer: Boolean

    fun cloneConfiguration() : ServerConfiguration = clone() as ServerConfiguration
}