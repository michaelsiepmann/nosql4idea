package org.codinjutsu.tools.nosql.commons.model.scheme

import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.model.DataType.STRING

internal class SchemeItem(val name: String, val type: DataType, val children: Collection<SchemeItem> = emptyList()) {

    val hasChildren: Boolean
        get() = children.isEmpty()

    val childNames: List<String>
        get() = children.map { it.name }

    fun findItem(path: List<String>): SchemeItem? {
        if (path.isNotEmpty() && name == path[0]) {
            val subpath = path.drop(1)
            if (subpath.isEmpty()) {
                return this
            }
            for (child in children) {
                val result = child.findItem(subpath)
                if (result != null) {
                    return result
                }
            }
        }
        return null
    }

    override fun toString() = name

    companion object {
        val EMPTY_SCHEME = SchemeItem("EMPTY", STRING)
    }
}