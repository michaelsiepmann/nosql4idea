package org.codinjutsu.tools.nosql.commons.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class DatabaseElementObjectWrapper(private val name: String, private val element: DatabaseElement) : ObjectWrapper {

    override val names: Collection<String>
        get() = listOf(name)

    override fun get(name: String): Any? {
        return element
    }
}