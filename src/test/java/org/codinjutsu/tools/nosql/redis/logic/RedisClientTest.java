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

package org.codinjutsu.tools.nosql.redis.logic;

import org.codinjutsu.tools.nosql.DatabaseVendor;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfigurationImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl;
import org.codinjutsu.tools.nosql.redis.RedisClientStub;
import org.codinjutsu.tools.nosql.redis.RedisContext;
import org.codinjutsu.tools.nosql.redis.model.RedisDatabase;
import org.codinjutsu.tools.nosql.redis.model.RedisKeyType;
import org.codinjutsu.tools.nosql.redis.model.RedisResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.HashSet;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisClientTest {

    private Jedis jedis;

    @Test
    void loadWithEmptyFilter() {
        RedisClient redisClient = new RedisClientStub(jedis);
        ServerConfiguration serverConfiguration = new ServerConfigurationImpl();
        serverConfiguration.setDatabaseVendor(DatabaseVendor.REDIS);
        serverConfiguration.setServerUrl("localhost:6379");

        QueryOptionsImpl queryOptions = new QueryOptionsImpl();
        queryOptions.setFilter("*");
        queryOptions.setResultLimit(300);

        RedisResult result = redisClient.loadRecords(new RedisContext(redisClient, serverConfiguration, new RedisDatabase("1")), queryOptions);
        verify(jedis, times(1)).connect();
        verify(jedis, times(1)).select(1);

        assertEquals(Arrays.asList("value1", "value2"), result.getResults().get(0).getValue());
    }

    @BeforeEach
    void setUp() {
        jedis = mock(Jedis.class);
        when(jedis.keys("*")).thenReturn(new HashSet<>(singletonList("testlist")));
        when(jedis.type("testlist")).thenReturn(RedisKeyType.LIST.label);
        when(jedis.lrange("testlist", 0, -1)).thenReturn(Arrays.asList("value1", "value2"));
    }
}
