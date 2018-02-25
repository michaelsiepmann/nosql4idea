package org.codinjutsu.tools.nosql.mongo.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.AbstractDatabaseContext
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient

class MongoContext(mongoClient: MongoClient, serverConfiguration: ServerConfiguration, val mongoCollection: MongoCollection) :
        AbstractDatabaseContext<MongoClient>(mongoClient, serverConfiguration)
