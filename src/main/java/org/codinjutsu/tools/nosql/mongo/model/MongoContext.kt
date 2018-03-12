package org.codinjutsu.tools.nosql.mongo.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AbstractDatabaseContext
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient

internal class MongoContext(mongoClient: MongoClient, serverConfiguration: ServerConfiguration, val mongoCollection: MongoCollection) :
        AbstractDatabaseContext(mongoClient, serverConfiguration)
