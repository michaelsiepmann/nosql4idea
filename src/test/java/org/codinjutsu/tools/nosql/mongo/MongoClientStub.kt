package org.codinjutsu.tools.nosql.mongo

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient

class MongoClientStub(private val mongoClient: com.mongodb.MongoClient) : MongoClient() {

    override fun createMongoClient(configuration: ServerConfiguration) = mongoClient
}
