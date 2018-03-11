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

package org.codinjutsu.tools.nosql.redis.logic

import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl
import org.codinjutsu.tools.nosql.redis.RedisClientStub
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration
import org.codinjutsu.tools.nosql.redis.model.RedisContext
import org.codinjutsu.tools.nosql.redis.model.RedisKeyType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import redis.clients.jedis.Jedis
import java.util.*

internal class RedisClientTest {

    private var jedis: Jedis? = null

    @Test
    fun loadWithEmptyFilter() {
        val redisClient = RedisClientStub(jedis!!)
        val serverConfiguration = RedisServerConfiguration()
        serverConfiguration.set(null, "localhost:6379")

        val queryOptions = QueryOptionsImpl()
        queryOptions.filter = "*"
        queryOptions.resultLimit = 300

        val result = redisClient.loadRecords(RedisContext(redisClient, serverConfiguration, Database("1")), queryOptions)
        verify<Jedis>(jedis, times(1)).connect()
        verify<Jedis>(jedis, times(1)).select(1)

        assertEquals(Arrays.asList("value1", "value2"), result.records.get(0))
    }

    @BeforeEach
    fun setUp() {
        jedis = mock(Jedis::class.java)
        `when`(jedis!!.keys("*")).thenReturn(HashSet(listOf("testlist")))
        `when`(jedis!!.type("testlist")).thenReturn(RedisKeyType.LIST.label)
        `when`(jedis!!.lrange("testlist", 0, -1)).thenReturn(Arrays.asList("value1", "value2"))
    }
}
