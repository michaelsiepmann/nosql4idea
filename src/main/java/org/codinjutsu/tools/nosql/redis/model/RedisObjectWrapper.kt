package org.codinjutsu.tools.nosql.redis.model

import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class RedisObjectWrapper<T>(private val redisRecord: RedisRecord<T>) : ObjectWrapper {
    override val names: Collection<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun get(name: String): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isArray(value: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isObject(value: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}