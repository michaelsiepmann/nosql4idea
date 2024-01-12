package org.apache.commons.httpclient

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpUriRequest
import org.mockito.Matchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockWhen

internal fun createHttpClient(responseStatus: Int, responseBody: String): HttpClient {
    val httpClient = mock(HttpClient::class.java)
    mockWhen(httpClient.execute(any(HttpUriRequest::class.java))).thenAnswer { invocation ->
        val response = mock(HttpResponse::class.java)
        val statusLine = mock(StatusLine::class.java)
        mockWhen(statusLine.statusCode).thenReturn(responseStatus)
        mockWhen(response.statusLine).thenReturn(statusLine)
        val entity = mock(HttpEntity::class.java)
        mockWhen(entity.content).thenReturn(responseBody.byteInputStream())
        mockWhen(response.getEntity()).thenReturn(entity)
        response
    }
    return httpClient
}