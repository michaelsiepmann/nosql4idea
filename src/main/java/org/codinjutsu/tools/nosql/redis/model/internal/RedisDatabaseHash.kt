package org.codinjutsu.tools.nosql.redis.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal class RedisDatabaseHash(private val values : Map<String, DatabaseElement>) :DatabaseObject {

    override fun names() = values.keys

    override fun get(key: String) = values[key]
}