package org.codinjutsu.tools.nosql.redis

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.redis.logic.RedisClient
import redis.clients.jedis.Jedis

class RedisClientStub(private val jedis: Jedis) : RedisClient() {

    override fun createJedis(serverConfiguration: ServerConfiguration?) = jedis
}