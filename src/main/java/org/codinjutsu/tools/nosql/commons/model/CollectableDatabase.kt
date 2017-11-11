package org.codinjutsu.tools.nosql.commons.model

internal interface CollectableDatabase<out T : NoSQLCollection> {

    fun getCollections(): Collection<T>
}