package org.codinjutsu.tools.nosql.solr.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendor.SOLR
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

internal class SolrServerConfiguration(
        label: String? = null,
        serverUrl: String = SOLR.defaultUrl,
        userDatabase: String? = null,
        isConnectOnIdeStartup: Boolean = false,
        authenticationSettings: AuthenticationSettings = AuthenticationSettings()
) : WriteableServerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings) {

    override val databaseVendor: DatabaseVendor
        get() = SOLR

    override fun copy(): ServerConfiguration =
            SolrServerConfiguration(label, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings)
}