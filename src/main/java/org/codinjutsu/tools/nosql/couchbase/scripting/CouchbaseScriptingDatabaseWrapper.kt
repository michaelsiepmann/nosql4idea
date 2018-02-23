package org.codinjutsu.tools.nosql.couchbase.scripting

import com.couchbase.client.java.document.json.JsonObject
import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptingDatabaseWrapper
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext

internal class CouchbaseScriptingDatabaseWrapper(private val context: CouchbaseContext) : ScriptingDatabaseWrapper<JsonObject> {

    override fun save(document: JsonObject) {
        context.client.update(context, document)
    }
}