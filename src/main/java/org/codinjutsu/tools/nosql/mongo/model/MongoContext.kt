package org.codinjutsu.tools.nosql.mongo.model

import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection

class MongoContext(mongoClient: MongoClient, serverConfiguration: ServerConfiguration, val mongoCollection: MongoCollection) :
        DatabaseContext<MongoClient>(mongoClient, serverConfiguration)
