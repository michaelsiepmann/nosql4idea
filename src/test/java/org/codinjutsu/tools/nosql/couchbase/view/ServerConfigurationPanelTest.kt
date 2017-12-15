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

package org.codinjutsu.tools.nosql.couchbase.view

import com.intellij.openapi.command.impl.DummyProject
import org.codinjutsu.tools.nosql.DatabaseVendor
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration
import org.codinjutsu.tools.nosql.couchbase.configuration.CouchbaseServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import org.codinjutsu.tools.nosql.commons.view.ServerConfigurationPanel
import org.fest.swing.edt.GuiActionRunner
import org.fest.swing.edt.GuiQuery
import org.fest.swing.fixture.Containers
import org.fest.swing.fixture.FrameFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class ServerConfigurationPanelTest {

    private var configurationPanel: ServerConfigurationPanel? = null
    private lateinit var databaseClientMock: DatabaseClient<*, *>

    private lateinit var frameFixture: FrameFixture

    @BeforeEach
    fun setUp() {
        databaseClientMock = Mockito.mock(DatabaseClient::class.java)
        configurationPanel = GuiActionRunner.execute(object : GuiQuery<ServerConfigurationPanel>() {
            override fun executeInEDT(): ServerConfigurationPanel {
                return ServerConfigurationPanel(DummyProject.getInstance(),
                        DatabaseVendor.COUCHBASE,
                        databaseClientMock,
                        CouchbaseAuthenticationPanel()
                )
            }
        })

        frameFixture = Containers.showInFrame(configurationPanel!!)
    }

    @AfterEach
    fun tearDown() {
        frameFixture.cleanUp()
    }

    @Test
    fun createCouchbaseConfiguration() {
        frameFixture.apply {
            textBox("labelField").setText("Localhost")

            label("databaseVendorLabel").requireText("Couchbase")
            label("databaseTipsLabel").requireText("format: host:port. If cluster: host:port1,host:port2,...")

            textBox("serverUrlField").setText("localhost:25")
            textBox("usernameField").setText("john")
            textBox("passwordField").setText("johnpassword")

            textBox("userDatabaseField").setText("mybucket")
            checkBox("autoConnectField").select()
        }

        val configuration = CouchbaseServerConfiguration()

        configurationPanel?.applyConfigurationData(configuration as WriteableServerConfiguration)

        assertEquals("Localhost", configuration.label)
        assertEquals(DatabaseVendor.COUCHBASE, configuration.databaseVendor)
        assertEquals("localhost:25", configuration.serverUrl)

        val authenticationSettings = configuration.authenticationSettings
        assertEquals("john", authenticationSettings.username)
        assertEquals("johnpassword", authenticationSettings.password)

        assertEquals("mybucket", configuration.userDatabase)
        assertTrue(configuration.isConnectOnIdeStartup)
    }

    @Test
    fun loadCouchbaseConfiguration() {
        val configuration = CouchbaseServerConfiguration(label = "Localhost", serverUrl = "localhost:25")

        val authenticationSettings = AuthenticationSettings()
        authenticationSettings.username = "john"
        authenticationSettings.password = "johnpassword"
        configuration.authenticationSettings = authenticationSettings

        configurationPanel?.loadConfigurationData(configuration)

        frameFixture.textBox("labelField").requireText("Localhost")
        frameFixture.label("databaseVendorLabel").requireText("Couchbase")
        frameFixture.label("databaseTipsLabel").requireText("format: host:port. If cluster: host:port1,host:port2,...")
        frameFixture.textBox("serverUrlField").requireText("localhost:25")
        frameFixture.textBox("usernameField").requireText("john")
        frameFixture.textBox("passwordField").requireText("johnpassword")
    }
}
