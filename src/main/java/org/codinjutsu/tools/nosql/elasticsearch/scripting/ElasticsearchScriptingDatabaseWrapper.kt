package org.codinjutsu.tools.nosql.elasticsearch.scripting

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptingDatabaseWrapper
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext

internal class ElasticsearchScriptingDatabaseWrapper(
        private val context: ElasticsearchContext
) : ScriptingDatabaseWrapper<JsonObject> {

    override fun save(document: JsonObject) {
        context.client.update(context, document)
    }
}