package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

abstract class WriteableConsoleRunnerConfiguration(
        label: String? = null,
        serverUrl: String,
        userDatabase: String? = null,
        isConnectOnIdeStartup: Boolean = false,
        authenticationSettings: AuthenticationSettings = AuthenticationSettings(),
        override var shellArgumentsLine: String? = null,
        override var shellWorkingDir: String? = null
) : WriteableServerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings), ConsoleRunnerConfiguration