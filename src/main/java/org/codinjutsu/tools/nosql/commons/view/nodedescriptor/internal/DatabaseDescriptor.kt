package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseObject

internal interface DatabaseDescriptor {
    fun buildObject(databaseObject: DatabaseObject)
}