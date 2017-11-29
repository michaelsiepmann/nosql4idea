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

package org.codinjutsu.tools.nosql.redis.model

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper
import redis.clients.jedis.Tuple

import java.util.LinkedList
import java.util.function.Function
import java.util.stream.Collectors

class RedisResult : SearchResult {

    private val redisRecords = LinkedList<RedisRecord<*>>()

    val results: List<RedisRecord<*>>
        get() = redisRecords

    override val name: String
        get() = "" // todo

    override val records: List<ObjectWrapper>
        get() = redisRecords.map { RedisObjectWrapper(it) }

    fun addString(key: String, value: String) {
        redisRecords.add(RedisRecord(RedisKeyType.STRING, key, value))
    }

    fun addList(key: String, values: List<*>) {
        redisRecords.add(RedisRecord(RedisKeyType.LIST, key, values))
    }

    fun addSet(key: String, values: Set<*>) {
        redisRecords.add(RedisRecord(RedisKeyType.SET, key, values))
    }

    fun addHash(key: String, values: Map<*, *>) {
        redisRecords.add(RedisRecord(RedisKeyType.HASH, key, values))
    }

    fun addSortedSet(key: String, values: Set<Tuple>) {
        redisRecords.add(RedisRecord(RedisKeyType.ZSET, key, values))
    }
}
