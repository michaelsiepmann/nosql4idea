package org.codinjutsu.tools.nosql.elasticsearch.logic.commands

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpClientStub
import org.junit.Test

class GetIndicesTest {

    private val httpClient: HttpClient = HttpClientStub(200, "")

    @Test
    fun execute() {
        val result = GetIndicesStub(httpClient).execute()
    }

    private class GetIndicesStub(private val httpClient: HttpClient) : GetIndices("") {

        override fun createClient() = httpClient
    }
}