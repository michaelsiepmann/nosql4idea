package org.codinjutsu.tools.nosql.elasticsearch.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendor.ELASTICSEARCH
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion

class ElasticsearchServerConfiguration(
        version: ElasticsearchVersion = ElasticsearchVersion.VERSION_20,
        label: String? = null,
        serverUrl: String = ELASTICSEARCH.defaultUrl,
        userDatabase: String? = null,
        isConnectOnIdeStartup: Boolean = false,
        authenticationSettings: AuthenticationSettings = AuthenticationSettings()
) : WriteableServerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings) {

    var version: ElasticsearchVersion = ElasticsearchVersion.VERSION_20

    init {
        this.version = version
    }

    override val databaseVendor: DatabaseVendor
        get() = ELASTICSEARCH

    override fun copy(): ServerConfiguration {
        return ElasticsearchServerConfiguration(version, label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings)
    }
}
