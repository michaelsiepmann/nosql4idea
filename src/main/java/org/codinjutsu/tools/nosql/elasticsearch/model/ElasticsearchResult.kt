package org.codinjutsu.tools.nosql.elasticsearch.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class ElasticsearchResult(override val name: String, searchResult: JsonObject) : SearchResult {

    private val objectWrappers: List<ObjectWrapper>
    private val totalCount : Int

    init {
        val hits = searchResult.getAsJsonObject("hits")
        val jsonArray = hits?.getAsJsonArray("hits") ?: JsonArray()
        objectWrappers = jsonArray.map { ElasticsearchObjectWrapper(it.asJsonObject) }
        totalCount = hits?.get("total")?.asInt ?: 0
    }

    override val records: List<ObjectWrapper>
        get() = objectWrappers

    override val resultCount: Int
        get() = totalCount

}
