package org.codinjutsu.tools.nosql.commons.model

internal interface NoSQLCollection {

    val name: String

    fun canBeDeleted() = true
}