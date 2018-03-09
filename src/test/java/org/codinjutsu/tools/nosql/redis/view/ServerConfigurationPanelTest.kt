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

package org.codinjutsu.tools.nosql.redis.view

import com.intellij.openapi.command.impl.DummyProject
import org.codinjutsu.tools.nosql.DatabaseVendor.REDIS
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings
import org.codinjutsu.tools.nosql.commons.view.ServerConfigurationPanel
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration
import org.codinjutsu.tools.nosql.redis.view.authentication.RedisAuthenticationPanel
import org.fest.swing.edt.GuiActionRunner
import org.fest.swing.edt.GuiQuery
import org.fest.swing.fixture.Containers
import org.fest.swing.fixture.FrameFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

internal class ServerConfigurationPanelTest {

    private var configurationPanel: ServerConfigurationPanel? = null
    private var databaseClientMock: DatabaseClient<*>? = null

    private var frameFixture: FrameFixture? = null

    @BeforeEach
    fun setUp() {
        databaseClientMock = mock(DatabaseClient::class.java)
        configurationPanel = GuiActionRunner.execute(object : GuiQuery<ServerConfigurationPanel>() {
            override fun executeInEDT(): ServerConfigurationPanel? {
                return ServerConfigurationPanel(DummyProject.getInstance(), REDIS, RedisAuthenticationPanel(), true)
            }
        })

        frameFixture = Containers.showInFrame(configurationPanel!!)
    }

    @AfterEach
    fun tearDown() {
        frameFixture!!.cleanUp()
    }

    @Test
    fun createRedisConfiguration() {
        frameFixture!!.textBox("labelField").setText("Localhost")

        frameFixture!!.label("databaseVendorLabel").requireText("RedisDB")
        frameFixture!!.label("databaseTipsLabel").requireText("format: host:port. If cluster: host:port1,host:port2,...")

        frameFixture!!.textBox("serverUrlField").setText("localhost:25")
        frameFixture!!.textBox("passwordField").setText("johnpassword")

        frameFixture!!.textBox("userDatabaseField").setText("0")
        frameFixture!!.checkBox("autoConnectField").select()

        val configuration = RedisServerConfiguration()

        configurationPanel!!.applyConfigurationData(configuration as WriteableServerConfiguration)

        assertEquals("Localhost", configuration.label)
        assertEquals(REDIS, configuration.databaseVendor)
        assertEquals("localhost:25", configuration.serverUrl)

        val authenticationSettings = configuration.authenticationSettings
        assertEquals("johnpassword", authenticationSettings.password)

        assertEquals("0", configuration.userDatabase)
        assertTrue(configuration.isConnectOnIdeStartup)
    }

    @Test
    fun loadRedisConfiguration() {
        val configuration = RedisServerConfiguration()
        configuration.set("Localhost", "localhost:6379")

        val authenticationSettings = AuthenticationSettings()
        authenticationSettings.password = "johnpassword"
        configuration.authenticationSettings = authenticationSettings

        configurationPanel!!.loadConfigurationData(configuration)

        frameFixture!!.textBox("labelField").requireText("Localhost")
        frameFixture!!.label("databaseVendorLabel").requireText("RedisDB")
        frameFixture!!.label("databaseTipsLabel").requireText("format: host:port. If cluster: host:port1,host:port2,...")
        frameFixture!!.textBox("serverUrlField").requireText("localhost:6379")
        frameFixture!!.textBox("passwordField").requireText("johnpassword")
    }
}
