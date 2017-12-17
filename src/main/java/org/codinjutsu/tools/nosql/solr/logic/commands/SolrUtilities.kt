package org.codinjutsu.tools.nosql.solr.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractHttpClientCommand
import org.codinjutsu.tools.nosql.solr.view.SolrContext

internal fun AbstractHttpClientCommand.urlWithCollection(context: SolrContext): String {
    return context.serverConfiguration.serverUrl
            .addNameToPath(context.solrDatabase.name)
}
