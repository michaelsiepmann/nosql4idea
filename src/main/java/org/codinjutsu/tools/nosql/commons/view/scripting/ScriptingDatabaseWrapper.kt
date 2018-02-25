package org.codinjutsu.tools.nosql.commons.view.scripting

import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext

internal class ScriptingDatabaseWrapper<in DOCUMENT>(
        private val context: DatabaseContext
) {
    @Suppress("unused")
    fun save(document: DOCUMENT) {
        val client: DatabaseClient<DOCUMENT> = context.client as DatabaseClient<DOCUMENT>
        client.update(context, document)
    }
}