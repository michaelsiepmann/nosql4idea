package org.codinjutsu.tools.nosql.mongo.model.internal

import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions

internal class DelegatingMongoDatabaseClient(private val delegate: DatabaseClient<DBObject>) : DatabaseClient<DatabaseElement> {
    override fun connect(serverConfiguration: ServerConfiguration?) {
        delegate.connect(serverConfiguration)
    }

    override fun loadServer(databaseServer: DatabaseServer?) {
        delegate.loadServer(databaseServer)
    }

    override fun cleanUpServers() {
        delegate.cleanUpServers()
    }

    override fun registerServer(databaseServer: DatabaseServer?) {
        delegate.registerServer(databaseServer)
    }

    override fun defaultConfiguration() = delegate.defaultConfiguration()

    override fun findAll(context: DatabaseContext) =
            DelegatingMongoSearchResult(delegate.findAll(context.getDelegatedContext()))

    override fun findDocument(context: DatabaseContext, _id: Any): DatabaseElement? {
        val id = if (_id is DatabasePrimitive) {
            _id.value() ?: _id
        } else {
            _id
        }
        return convert(delegate.findDocument(context.getDelegatedContext(), id))
    }

    override fun loadRecords(context: DatabaseContext, query: QueryOptions) =
            DelegatingMongoSearchResult(delegate.loadRecords(context.getDelegatedContext(), query))

    override fun update(context: DatabaseContext, document: DatabaseElement) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(context: DatabaseContext, _id: Any) {
        delegate.delete(context.getDelegatedContext(), _id)
    }

    override fun getServers(): Collection<DatabaseServer> = delegate.servers
}