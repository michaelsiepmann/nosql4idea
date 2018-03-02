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

package org.codinjutsu.tools.nosql.redis.view;

import com.intellij.openapi.command.impl.DummyProject;
import org.codinjutsu.tools.nosql.DatabaseVendor;
import org.codinjutsu.tools.nosql.commons.configuration.WriteableServerConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.AuthenticationSettings;
import org.codinjutsu.tools.nosql.commons.view.ServerConfigurationPanel;
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration;
import org.codinjutsu.tools.nosql.redis.view.authentication.RedisAuthenticationPanel;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.Containers;
import org.fest.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerConfigurationPanelTest {

    private ServerConfigurationPanel configurationPanel;
    private DatabaseClient databaseClientMock;

    private FrameFixture frameFixture;

    @BeforeEach
    void setUp() {
        databaseClientMock = Mockito.mock(DatabaseClient.class);
        configurationPanel = GuiActionRunner.execute(new GuiQuery<ServerConfigurationPanel>() {
            protected ServerConfigurationPanel executeInEDT() {
                return new ServerConfigurationPanel(DummyProject.getInstance(),
                        DatabaseVendor.REDIS,
                        new RedisAuthenticationPanel(), true
                );
            }
        });

        frameFixture = Containers.showInFrame(configurationPanel);
    }

    @AfterEach
    void tearDown() {
        frameFixture.cleanUp();
    }

    @Test
    void createRedisConfiguration() {
        frameFixture.textBox("labelField").setText("Localhost");

        frameFixture.label("databaseVendorLabel").requireText("RedisDB");
        frameFixture.label("databaseTipsLabel").requireText("format: host:port. If cluster: host:port1,host:port2,...");

        frameFixture.textBox("serverUrlField").setText("localhost:25");
        frameFixture.textBox("passwordField").setText("johnpassword");

        frameFixture.textBox("userDatabaseField").setText("0");
        frameFixture.checkBox("autoConnectField").select();

        ServerConfiguration configuration = new RedisServerConfiguration();

        configurationPanel.applyConfigurationData((WriteableServerConfiguration) configuration);

        assertEquals("Localhost", configuration.getLabel());
        assertEquals(DatabaseVendor.REDIS, configuration.getDatabaseVendor());
        assertEquals("localhost:25", configuration.getServerUrl());

        AuthenticationSettings authenticationSettings = configuration.getAuthenticationSettings();
        assertEquals("johnpassword", authenticationSettings.getPassword());

        assertEquals("0", configuration.getUserDatabase());
        assertTrue(configuration.isConnectOnIdeStartup());
    }

    @Test
    void loadRedisConfiguration() {
        WriteableServerConfiguration configuration = new RedisServerConfiguration();
        configuration.set("Localhost", "localhost:6379");

        AuthenticationSettings authenticationSettings = new AuthenticationSettings();
        authenticationSettings.setPassword("johnpassword");
        configuration.setAuthenticationSettings(authenticationSettings);

        configurationPanel.loadConfigurationData(configuration);

        frameFixture.textBox("labelField").requireText("Localhost");
        frameFixture.label("databaseVendorLabel").requireText("RedisDB");
        frameFixture.label("databaseTipsLabel").requireText("format: host:port. If cluster: host:port1,host:port2,...");
        frameFixture.textBox("serverUrlField").requireText("localhost:6379");
        frameFixture.textBox("passwordField").requireText("johnpassword");
    }
}
