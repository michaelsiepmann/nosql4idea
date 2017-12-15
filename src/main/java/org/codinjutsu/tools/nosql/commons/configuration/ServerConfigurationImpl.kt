/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.commons.configuration

import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.DatabaseVendor.ELASTICSEARCH
import org.codinjutsu.tools.nosql.DatabaseVendor.MONGO
import org.codinjutsu.tools.nosql.DatabaseVendor.REDIS
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import org.codinjutsu.tools.nosql.elasticsearch.configuration.ElasticsearchServerConfiguration
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchVersion.VERSION_20
import org.codinjutsu.tools.nosql.mongo.configuration.MongoServerConfiguration
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration

open class ServerConfigurationImpl(
        override var label: String? = null,
        override var databaseVendor: DatabaseVendor = DatabaseVendor.MONGO,
        override var serverUrl: String? = null,
        override var userDatabase: String? = null,
        override var isConnectOnIdeStartup: Boolean = false,
        override var authenticationSettings: AuthenticationSettings = AuthenticationSettings()
) : ServerConfiguration {

    constructor(databaseVendor: DatabaseVendor) : this(databaseVendor = databaseVendor, serverUrl = databaseVendor.defaultUrl)

    override val isSingleServer: Boolean
        get() = serverUrl!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1

    override fun copy(): ServerConfiguration =
            ServerConfigurationImpl(label, databaseVendor, serverUrl, userDatabase, isConnectOnIdeStartup, authenticationSettings)

    companion object {
        fun create(databaseVendor: DatabaseVendor) =
                when (databaseVendor) {
                    ELASTICSEARCH -> ElasticsearchServerConfiguration(VERSION_20)
                    MONGO -> MongoServerConfiguration()
                    REDIS -> RedisServerConfiguration()
                    else -> ServerConfigurationImpl(databaseVendor)
                }

        @JvmOverloads
        fun create(databaseVendor: DatabaseVendor = DatabaseVendor.MONGO, serverUrl: String?, authenticationSettings: AuthenticationSettings = AuthenticationSettings(), userDatabase: String? = null) =
                when (databaseVendor) {
                    ELASTICSEARCH -> ElasticsearchServerConfiguration(VERSION_20)
                    MONGO -> MongoServerConfiguration(serverUrl, userDatabase, authenticationSettings)
                    REDIS -> RedisServerConfiguration(serverUrl, userDatabase, authenticationSettings)
                    else -> ServerConfigurationImpl(databaseVendor = databaseVendor, serverUrl = serverUrl, authenticationSettings = authenticationSettings, userDatabase = userDatabase)
                }
    }
}
