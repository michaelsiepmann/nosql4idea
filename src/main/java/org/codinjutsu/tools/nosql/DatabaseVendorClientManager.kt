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

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.exceptions.DatabaseException
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

class DatabaseVendorClientManager(private val project: Project) {

    fun cleanUpServers() {
        DatabaseVendor.values()
                .mapNotNull {
                    it.databaseVendorInformation.getClient(project)
                }
                .forEach {
                    it.cleanUpServers()
                }
    }

    fun registerServer(databaseServer: DatabaseServer) {
        databaseClient(databaseServer)?.registerServer(databaseServer)
    }

    fun loadServer(databaseServer: DatabaseServer) {
        try {
            databaseClient(databaseServer)?.loadServer(databaseServer)
        } catch (e: Exception) {
            throw DatabaseException("", e)
        }
    }

    private fun databaseClient(databaseServer: DatabaseServer): DatabaseClient? =
            databaseServer.vendor.databaseVendorInformation.getClient(project)

    companion object {
        fun getInstance(project: Project): DatabaseVendorClientManager =
                ServiceManager.getService(project, DatabaseVendorClientManager::class.java)
    }
}
