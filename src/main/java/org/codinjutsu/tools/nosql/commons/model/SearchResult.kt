package org.codinjutsu.tools.nosql.commons.model

import com.couchbase.client.java.document.json.JsonObject

internal interface SearchResult {

    val name: String

    val records: List<JsonObject>
}