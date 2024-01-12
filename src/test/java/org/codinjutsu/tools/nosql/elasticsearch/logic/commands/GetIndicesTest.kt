package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.createHttpClient
import org.apache.http.client.HttpClient
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetIndicesTest {

    private val httpClient = createHttpClient(200, RESPONSE)

    @Test
    fun execute() {
        val result = GetIndicesStub(httpClient).execute()
        assertEquals("test", result.get("index").asString)
        assertEquals("testtype", result.get("type").asString)
    }

    private class GetIndicesStub(private val httpClient: HttpClient) : GetIndices("/dummyindex") {

        override fun createClient() = httpClient
    }

    companion object {
        @Language("json")
        const val RESPONSE = """{
            "index": "test",
            "type": "testtype"
}"""
    }
}