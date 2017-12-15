package org.codinjutsu.tools.nosql.elasticsearch.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendor.ELASTICSEARCH
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfigurationImpl
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion

class ElasticsearchServerConfiguration(version: ElasticsearchVersion = ElasticsearchVersion.VERSION_20) : ServerConfigurationImpl() {

    constructor(version: ElasticsearchVersion, serverConfiguration: ServerConfiguration) : this(version) {
        label = serverConfiguration.label
        databaseVendor = serverConfiguration.databaseVendor
        serverUrl = serverConfiguration.serverUrl
        userDatabase = serverConfiguration.userDatabase
        isConnectOnIdeStartup = serverConfiguration.isConnectOnIdeStartup
        authenticationSettings = serverConfiguration.authenticationSettings
    }

    constructor(version: ElasticsearchVersion, label: String?, databaseVendor: DatabaseVendor, serverUrl: String?, userDatabase: String?, isConnectOnIdeStartup: Boolean, authenticationSettings: AuthenticationSettings) : this(version) {
        this.label = label
        this.databaseVendor = databaseVendor
        this.serverUrl = serverUrl
        this.userDatabase = userDatabase
        this.isConnectOnIdeStartup = isConnectOnIdeStartup
        this.authenticationSettings = authenticationSettings
    }

    var version: ElasticsearchVersion = ElasticsearchVersion.VERSION_20

    init {
        this.version = version
        this.databaseVendor = ELASTICSEARCH
        this.serverUrl = ELASTICSEARCH.defaultUrl
    }

    override fun copy(): ServerConfiguration {
        return ElasticsearchServerConfiguration(version, label, databaseVendor, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings)
    }
}
