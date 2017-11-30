package org.codinjutsu.tools.nosql.commons.model

internal interface Folder<T : NoSQLCollection> {

    fun getChildFolders(): Collection<T> = emptyList()

    fun addCollection(collection: T)
}