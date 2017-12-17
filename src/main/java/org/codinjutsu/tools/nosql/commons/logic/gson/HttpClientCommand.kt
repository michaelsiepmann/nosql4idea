package org.codinjutsu.tools.nosql.commons.logic.gson

import com.google.gson.JsonObject

internal interface HttpClientCommand {

    fun execute(): JsonObject
}