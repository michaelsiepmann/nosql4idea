package org.apache.commons.httpclient

internal class HttpClientStub(private val responseStatus: Int, private val responseBody: String) : HttpClient() {

    override fun executeMethod(method: HttpMethod?): Int {
        (method as HttpMethodBase).responseStream = responseBody.byteInputStream()
        return responseStatus
    }
}