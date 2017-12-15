package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

abstract class WriteableServerConfiguration(
        override var label: String? = null,
        override var serverUrl: String,
        override var userDatabase: String? = null,
        override var isConnectOnIdeStartup: Boolean = false,
        override var authenticationSettings: AuthenticationSettings = AuthenticationSettings()
) : ServerConfiguration {

    fun set(label: String?, serverUrl: String) {
        this.label = label
        this.serverUrl = serverUrl
    }

    override val isSingleServer: Boolean
        get() = serverUrl.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1
}