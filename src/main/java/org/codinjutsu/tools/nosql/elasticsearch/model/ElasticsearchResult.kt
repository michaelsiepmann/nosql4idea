package org.codinjutsu.tools.nosql.elasticsearch.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper
import java.util.*

internal class ElasticsearchResult(private val _name: String) : SearchResult {
    private val jsonObjects = LinkedList<ObjectWrapper>()
    private val errors = LinkedList<JsonObject>()

    override fun getName() = _name

    override fun getRecords() = jsonObjects

    fun add(jsonObject: JsonObject) {
        this.jsonObjects.add(ElasticsearchObjectWrapper(jsonObject))
    }

    fun addAll(jsonArray: JsonArray) {
        jsonArray.forEach {
            add(it.asJsonObject)
        }
    }

    fun addErrors(errors: List<JsonObject>) {
        this.errors.addAll(errors)
    }

    fun hasErrors() = !errors.isEmpty()

    fun getErrors() = errors
}
