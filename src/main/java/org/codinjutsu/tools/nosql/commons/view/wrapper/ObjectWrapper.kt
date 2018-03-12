package org.codinjutsu.tools.nosql.commons.view.wrapper

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal class ObjectWrapper(val names: Collection<String>, private val values: Map<String, DatabaseElement?>) {
    constructor(databaseObect: DatabaseObject) : this(databaseObect.names(), databaseObect.toMap())

    operator fun get(name: String) = values[name]
}