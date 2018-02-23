package org.codinjutsu.tools.nosql.solr.scripting

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptingDatabaseWrapper
import org.codinjutsu.tools.nosql.solr.model.SolrContext

internal class SolrScriptingDatabaseWrapper(private val context: SolrContext) : ScriptingDatabaseWrapper<JsonObject> {
    override fun save(document: JsonObject) {
        context.client.update(context, document)
    }
}