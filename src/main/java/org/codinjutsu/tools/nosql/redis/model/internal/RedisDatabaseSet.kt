package org.codinjutsu.tools.nosql.redis.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

internal class RedisDatabaseSet(private val values : Set<DatabaseElement>):DatabaseArray {

    override fun size() = values.size

    override fun get(index: Int) = values.elementAt(index)

    override fun iterator() = values.iterator()
}