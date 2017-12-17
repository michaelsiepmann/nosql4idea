package org.codinjutsu.tools.nosql.solr.logic.commands

import org.codinjutsu.tools.nosql.commons.logic.gson.AbstractGetCommand
import org.codinjutsu.tools.nosql.commons.model.DatabaseServer

internal class LoadCores(private val databaseServer: DatabaseServer) : AbstractGetCommand() {

    override fun buildURL(): String {
        return databaseServer.serverUrl + "/admin/cores"
    }
}