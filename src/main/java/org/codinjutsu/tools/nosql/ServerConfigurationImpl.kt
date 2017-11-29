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

package org.codinjutsu.tools.nosql

import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings

open class ServerConfigurationImpl(
        override var label: String? = null,
        override var databaseVendor: DatabaseVendor = DatabaseVendor.MONGO,
        override var serverUrl: String? = null,
        override var userDatabase: String? = null,
        override var isConnectOnIdeStartup: Boolean = false,
        override var shellArgumentsLine: String? = null,
        override var shellWorkingDir: String? = null,
        override var authenticationSettings: AuthenticationSettings = AuthenticationSettings()
) : ServerConfiguration {
    override val isSingleServer: Boolean
        get() = serverUrl!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1

    companion object {
        @JvmOverloads
        fun create(databaseVendor: DatabaseVendor = DatabaseVendor.MONGO, serverUrl: String? = null, authenticationSettings: AuthenticationSettings = AuthenticationSettings(), userDatabase: String? = null) =
                ServerConfigurationImpl(databaseVendor = databaseVendor, serverUrl = serverUrl, authenticationSettings = authenticationSettings, userDatabase = userDatabase)
    }
}
