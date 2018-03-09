package org.codinjutsu.tools.nosql.commons.model.internal

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer
import org.codinjutsu.tools.nosql.commons.model.internal.json.convert
import org.codinjutsu.tools.nosql.commons.model.internal.json.revert
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions

internal class JsonDatabaseClient(private val delegate: DatabaseClient<JsonObject>) :
        DatabaseClient<DatabaseElement> {

    override fun connect(serverConfiguration: ServerConfiguration) {
        delegate.connect(serverConfiguration)
    }

    override fun loadServer(databaseServer: DatabaseServer) {
        delegate.loadServer(databaseServer)
    }

    override fun cleanUpServers() {
        delegate.cleanUpServers()
    }

    override fun registerServer(databaseServer: DatabaseServer) {
        delegate.registerServer(databaseServer)
    }

    override fun defaultConfiguration() = delegate.defaultConfiguration()

    override fun findAll(context: DatabaseContext) = InternalSearchResult(delegate.findAll(context.getDelegatedContext()))

    override fun findDocument(context: DatabaseContext, _id: Any) = convert(delegate.findDocument(context.getDelegatedContext(), _id) as JsonElement)

    override fun loadRecords(context: DatabaseContext, query: QueryOptions?) =
            InternalSearchResult(delegate.loadRecords(context.getDelegatedContext(), query))

    override fun update(context: DatabaseContext, document: DatabaseElement) {
        revert(document)?.asJsonObject?.let {
            delegate.update(context.getDelegatedContext(), it)
        }
    }

    override fun delete(context: DatabaseContext, _id: Any) {
        delegate.delete(context.getDelegatedContext(), _id)
    }

    override fun getServers(): Collection<DatabaseServer> = delegate.servers

    override fun getScheme(context: DatabaseContext) = delegate.getScheme(context.getDelegatedContext())
}