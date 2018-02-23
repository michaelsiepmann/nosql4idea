package org.codinjutsu.tools.nosql.redis.scripting

import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptingDatabaseWrapper
import org.codinjutsu.tools.nosql.redis.model.RedisContext

internal class RedisScriptingDatabaseWrapper(private val context : RedisContext) : ScriptingDatabaseWrapper<Any> {

    override fun save(document: Any) {
        context.client.update(context, document)
    }
}