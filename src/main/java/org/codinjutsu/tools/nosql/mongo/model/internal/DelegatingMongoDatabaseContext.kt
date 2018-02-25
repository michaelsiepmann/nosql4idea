package org.codinjutsu.tools.nosql.mongo.model.internal

import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext

internal class DelegatingMongoDatabaseContext(private val delegate: DatabaseContext) : DatabaseContext by delegate {
    override val client: DatabaseClient<*>
        get() = DelegatingMongoDatabaseClient(delegate.client as DatabaseClient<DBObject>)
}