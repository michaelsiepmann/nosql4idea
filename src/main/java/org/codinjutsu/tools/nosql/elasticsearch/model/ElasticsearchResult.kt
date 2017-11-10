package org.codinjutsu.tools.nosql.elasticsearch.model

import com.couchbase.client.java.document.json.JsonObject
import org.codinjutsu.tools.nosql.commons.model.SearchResult

import java.util.LinkedList

class ElasticsearchResult(override val name: String) : SearchResult {
    private val jsonObjects = LinkedList<JsonObject>()
    private val errors = LinkedList<JsonObject>()

    override val records: List<JsonObject>
        get() = jsonObjects

    fun add(jsonObject: JsonObject) {
        this.jsonObjects.add(jsonObject)
    }

    fun addErrors(errors: List<JsonObject>) {
        this.errors.addAll(errors)
    }

    fun hasErrors() = !errors.isEmpty()

    fun getErrors() = errors
}
