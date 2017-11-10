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

data class ServerConfiguration(
        var label: String? = null,
        var databaseVendor: DatabaseVendor = DatabaseVendor.MONGO,
        var serverUrl: String? = null,
        var userDatabase: String? = null,
        var isConnectOnIdeStartup: Boolean = false,
        var shellArgumentsLine: String? = null,
        var shellWorkingDir: String? = null,
        var authenticationSettings: AuthenticationSettings = AuthenticationSettings()
) : Cloneable {
    val isSingleServer: Boolean
        get() = serverUrl!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1

    override fun clone() = copy()

    companion object {
        @JvmOverloads
        fun create(databaseVendor: DatabaseVendor = DatabaseVendor.MONGO, serverUrl: String? = null, authenticationSettings: AuthenticationSettings = AuthenticationSettings(), userDatabase: String? = null) =
                ServerConfiguration(databaseVendor = databaseVendor, serverUrl = serverUrl, authenticationSettings = authenticationSettings, userDatabase = userDatabase)
    }
}
