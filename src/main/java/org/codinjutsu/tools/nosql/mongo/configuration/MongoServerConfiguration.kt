package org.codinjutsu.tools.nosql.mongo.configuration

import org.codinjutsu.tools.nosql.commons.configuration.ConsoleRunnerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfigurationImpl
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

internal class MongoServerConfiguration() : ServerConfigurationImpl(), ConsoleRunnerConfiguration {

    constructor(serverUrl: String?, userDatabase: String? = null, authenticationSettings: AuthenticationSettings = AuthenticationSettings()) : this() {
        this.serverUrl = serverUrl
        this.userDatabase = userDatabase
        this.authenticationSettings = authenticationSettings
    }

    override var shellArgumentsLine: String? = null
    override var shellWorkingDir: String? = null
}