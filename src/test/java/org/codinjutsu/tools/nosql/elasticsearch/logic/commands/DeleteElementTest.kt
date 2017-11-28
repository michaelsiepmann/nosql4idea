package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpClientStub
import org.apache.commons.httpclient.methods.DeleteMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DeleteElementTest {

    private val httpClient: HttpClientStub = HttpClientStub(200, GetIndicesTest.RESPONSE)

    @Test
    fun execute() {
        val result = DeleteElementStub(httpClient, "/url").execute()
        assertTrue(httpClient.calledMethod is DeleteMethod)
        assertEquals("/url", httpClient.calledMethod?.uri.toString())
        assertEquals("test", result.get("index").asString)
        assertEquals("testtype", result.get("type").asString)
    }

    private class DeleteElementStub(private val httpClient: HttpClient, url: String) : DeleteElement(url) {

        override fun createClient() = httpClient
    }
}