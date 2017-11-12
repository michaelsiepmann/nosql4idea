package org.codinjutsu.tools.nosql.commons.model

internal interface Folder<out T : NoSQLCollection> {

    fun getChildFolders(): Collection<T>
}