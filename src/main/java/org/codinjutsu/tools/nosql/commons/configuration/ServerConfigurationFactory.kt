package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import org.codinjutsu.tools.nosql.couchbase.configuration.CouchbaseServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.configuration.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion
import org.codinjutsu.tools.nosql.mongo.configuration.MongoServerConfiguration
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration

@JvmOverloads
fun create(databaseVendor: DatabaseVendor = DatabaseVendor.MONGO, serverUrl: String, authenticationSettings: AuthenticationSettings = AuthenticationSettings(), userDatabase: String? = null) =
        when (databaseVendor) {
            DatabaseVendor.ELASTICSEARCH -> ElasticsearchServerConfiguration(ElasticsearchVersion.VERSION_20, serverUrl = serverUrl, userDatabase =  userDatabase, authenticationSettings = authenticationSettings)
            DatabaseVendor.MONGO -> MongoServerConfiguration(serverUrl = serverUrl, userDatabase = userDatabase, authenticationSettings = authenticationSettings)
            DatabaseVendor.REDIS -> RedisServerConfiguration(serverUrl = serverUrl, userDatabase = userDatabase, authenticationSettings = authenticationSettings)
            else -> CouchbaseServerConfiguration(serverUrl = serverUrl, authenticationSettings = authenticationSettings, userDatabase = userDatabase)
        }
