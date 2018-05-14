package org.codinjutsu.tools.nosql.commons.model.internal.layer

internal interface DatabaseNull : DatabaseElement {

    override fun isNull() = true
}