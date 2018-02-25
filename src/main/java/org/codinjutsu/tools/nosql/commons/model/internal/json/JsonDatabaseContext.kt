package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.internal.JsonDatabaseClient

internal class JsonDatabaseContext(private val delegate: DatabaseContext) : DatabaseContext by delegate {

    override val client: JsonDatabaseClient
        get() = JsonDatabaseClient(delegate.client as DatabaseClient<JsonObject>)
}