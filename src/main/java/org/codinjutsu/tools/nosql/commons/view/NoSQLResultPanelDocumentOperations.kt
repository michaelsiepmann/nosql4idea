package org.codinjutsu.tools.nosql.commons.view

interface NoSQLResultPanelDocumentOperations<T> {
    fun getDocument(_id: Any): T

    fun deleteDocument(document: Any)

    fun updateDocument(document: T)
}
