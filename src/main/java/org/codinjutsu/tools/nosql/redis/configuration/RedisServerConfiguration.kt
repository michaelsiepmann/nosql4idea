package org.codinjutsu.tools.nosql.redis.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor.REDIS
import org.codinjutsu.tools.nosql.commons.configuration.ConsoleRunnerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfigurationImpl
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

internal class RedisServerConfiguration() : ServerConfigurationImpl(REDIS), ConsoleRunnerConfiguration {
    constructor(serverUrl: String?, userDatabase: String? = null, authenticationSettings: AuthenticationSettings = AuthenticationSettings()) : this() {
        this.serverUrl = serverUrl
        this.userDatabase = userDatabase
        this.authenticationSettings = authenticationSettings
    }

    override var shellArgumentsLine: String? = null
    override var shellWorkingDir: String? = null
}