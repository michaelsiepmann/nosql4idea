package org.codinjutsu.tools.nosql.commons.view.scripting

internal interface ScriptingDatabaseWrapper<in DOCUMENT> {

    fun save(document: DOCUMENT)
}