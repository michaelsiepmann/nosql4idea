package org.codinjutsu.tools.nosql.redis.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseArray
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import java.util.*

internal class RedisDatabaseSortedSet(private val values: SortedSet<DatabaseElement>) : DatabaseArray {

    override fun size() = values.size

    override fun get(index: Int) = values.elementAt(index)

    override fun iterator() = values.iterator()
}