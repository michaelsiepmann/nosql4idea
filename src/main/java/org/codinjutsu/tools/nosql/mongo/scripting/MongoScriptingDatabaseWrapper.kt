package org.codinjutsu.tools.nosql.mongo.scripting

import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptingDatabaseWrapper
import org.codinjutsu.tools.nosql.mongo.model.MongoContext

internal class MongoScriptingDatabaseWrapper(private val context: MongoContext) : ScriptingDatabaseWrapper<DBObject> {
    override fun save(document: DBObject) {
        context.client.update(context, document)
    }
}