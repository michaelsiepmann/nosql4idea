package org.codinjutsu.tools.nosql.commons.model.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class DatabaseElementObjectWrapper(private val jsonObject: DatabaseObject) : ObjectWrapper {
    override val names: Collection<String>
        get() = jsonObject.names()

    override fun get(name: String): DatabaseElement? = jsonObject.get(name)
}