package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractHttpClientCommand
import org.codinjutsu.tools.nosql.elasticsearch.view.ElasticsearchContext

internal fun AbstractHttpClientCommand.urlWithIndexAndType(context: ElasticsearchContext): String {
    return context.serverConfiguration.serverUrl
            .addNameToPath(context.database.name)
            .addNameToPath(context.type?.name)
}
