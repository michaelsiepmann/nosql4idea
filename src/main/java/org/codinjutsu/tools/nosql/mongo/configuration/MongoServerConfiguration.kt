package org.codinjutsu.tools.nosql.mongo.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendor.MONGO
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.WriteableConsoleRunnerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

class MongoServerConfiguration(
        label: String? = null,
        serverUrl: String = MONGO.defaultUrl,
        userDatabase: String? = null,
        isConnectOnIdeStartup: Boolean = false,
        authenticationSettings: AuthenticationSettings = AuthenticationSettings(),
        shellArgumentsLine: String? = null,
        shellWorkingDir: String? = null
) : WriteableConsoleRunnerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings, shellArgumentsLine, shellWorkingDir) {

    override val databaseVendor: DatabaseVendor
        get() = MONGO

    override fun copy(): ServerConfiguration {
        return MongoServerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings, shellArgumentsLine, shellWorkingDir)
    }
}