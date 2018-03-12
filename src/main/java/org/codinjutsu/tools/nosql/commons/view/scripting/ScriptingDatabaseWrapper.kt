package org.codinjutsu.tools.nosql.commons.view.scripting

import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement

internal class ScriptingDatabaseWrapper(
        private val context: DatabaseContext
) {
    fun save(document: DatabaseElement) {
        context.client.update(context, document)
    }
}