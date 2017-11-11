package org.codinjutsu.tools.nosql.elasticsearch.model

import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

import java.util.LinkedList

internal class ElasticsearchResult(override val name: String) : SearchResult {
    private val jsonObjects = LinkedList<ObjectWrapper>()
    private val errors = LinkedList<JsonObject>()

    override val records: List<ObjectWrapper>
        get() = jsonObjects

    fun add(jsonObject: JsonObject) {
        this.jsonObjects.add(ElasticsearchObjectWrapper(jsonObject))
    }

    fun addErrors(errors: List<JsonObject>) {
        this.errors.addAll(errors)
    }

    fun hasErrors() = !errors.isEmpty()

    fun getErrors() = errors
}
