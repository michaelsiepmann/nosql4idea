package org.codinjutsu.tools.nosql.redis.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendor.REDIS
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.WriteableConsoleRunnerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

class RedisServerConfiguration(
        label: String? = null,
        serverUrl: String = REDIS.defaultUrl,
        userDatabase: String? = null,
        isConnectOnIdeStartup: Boolean = false,
        authenticationSettings: AuthenticationSettings = AuthenticationSettings(),
        shellArgumentsLine: String? = null,
        shellWorkingDir: String? = null
) : WriteableConsoleRunnerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings, shellArgumentsLine, shellWorkingDir) {

    override val databaseVendor: DatabaseVendor
        get() = REDIS

    override fun copy(): ServerConfiguration {
        return RedisServerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings, shellArgumentsLine, shellWorkingDir)
    }
}