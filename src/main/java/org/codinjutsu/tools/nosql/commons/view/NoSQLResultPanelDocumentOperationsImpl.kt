package org.codinjutsu.tools.nosql.commons.view

import org.codinjutsu.tools.nosql.ServerConfiguration
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient
import org.codinjutsu.tools.nosql.commons.model.SearchResult

class NoSQLResultPanelDocumentOperationsImpl<SERVERCONFIGURATION : ServerConfiguration, out CLIENT : DatabaseClient<CONTEXT, DOCUMENT, SERVERCONFIGURATION>, CONTEXT : DatabaseContext<CLIENT, SERVERCONFIGURATION>, RESULT : SearchResult, DOCUMENT>(private val panel: DatabasePanel<SERVERCONFIGURATION, CLIENT, CONTEXT, RESULT, DOCUMENT>) : NoSQLResultPanelDocumentOperations<DOCUMENT> {

    override fun getDocument(_id: Any): DOCUMENT? = panel.context.client.findDocument(panel.context, _id)

    override fun deleteDocument(document: Any) {
        panel.context.client.delete(panel.context, document)
        panel.executeQuery()
    }

    override fun updateDocument(document: DOCUMENT) {
        panel.context.client.update(panel.context, document)
        panel.executeQuery()
    }
}