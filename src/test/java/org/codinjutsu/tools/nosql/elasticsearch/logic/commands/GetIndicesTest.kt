package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpClientStub
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetIndicesTest {

    private val httpClient: HttpClient = HttpClientStub(200, RESPONSE)

    @Test
    fun execute() {
        val result = GetIndicesStub(httpClient).execute()
        assertEquals("test", result.get("index").asString)
        assertEquals("testtype", result.get("type").asString)
    }

    private class GetIndicesStub(private val httpClient: HttpClient) : GetIndices("") {

        override fun createClient() = httpClient
    }

    companion object {
        @Language("json")
        const val RESPONSE = """{
            "index": "test",
            "type": "testtype"
}""";
    }
}