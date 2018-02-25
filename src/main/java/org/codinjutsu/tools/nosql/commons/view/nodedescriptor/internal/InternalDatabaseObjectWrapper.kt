package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class InternalDatabaseObjectWrapper(private val value: DatabaseObject) : ObjectWrapper {
    override val names = value.names()

    override fun get(name: String) = value.get(name)
}