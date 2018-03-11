package org.codinjutsu.tools.nosql.redis.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

internal class RedisDatabaseList(private val values: List<DatabaseElement>) : DatabaseArray {

    override fun size() = values.size

    override fun get(index: Int) = values[index]

    override fun toArray() = values.iterator()
}