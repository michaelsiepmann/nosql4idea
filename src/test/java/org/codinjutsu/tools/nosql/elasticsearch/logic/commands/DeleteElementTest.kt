package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.createHttpClient
import org.apache.http.client.HttpClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DeleteElementTest {

    private val httpClient = createHttpClient(200, GetIndicesTest.RESPONSE)

    @Test
    fun execute() {
        val result = DeleteElementStub(httpClient, "/url").execute()
        assertEquals("test", result.get("index").asString)
        assertEquals("testtype", result.get("type").asString)
    }

    private class DeleteElementStub(private val httpClient: HttpClient, url: String) : DeleteElement(url) {

        override fun createClient() = httpClient
    }
}