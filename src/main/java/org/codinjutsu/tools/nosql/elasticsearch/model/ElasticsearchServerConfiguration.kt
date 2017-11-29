package org.codinjutsu.tools.nosql.elasticsearch.model

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.ServerConfigurationImpl

class ElasticsearchServerConfiguration(version: ElasticsearchVersion = ElasticsearchVersion.VERSION_20) : ServerConfigurationImpl() {

    constructor(version: ElasticsearchVersion, serverUrl: String, databaseVendor: DatabaseVendor) : this(version) {
        this.serverUrl = serverUrl
        this.databaseVendor = databaseVendor
    }

    constructor(version: ElasticsearchVersion, serverConfiguration: ServerConfiguration): this(version) {
        label = serverConfiguration.label
        databaseVendor = serverConfiguration.databaseVendor
        serverUrl = serverConfiguration.serverUrl
        userDatabase = serverConfiguration.userDatabase
        isConnectOnIdeStartup = serverConfiguration.isConnectOnIdeStartup
        shellArgumentsLine = serverConfiguration.shellArgumentsLine
        shellWorkingDir = serverConfiguration.shellWorkingDir
        authenticationSettings = serverConfiguration.authenticationSettings
    }

    var version: ElasticsearchVersion = ElasticsearchVersion.VERSION_20

    init {
        this.version = version
    }
}
