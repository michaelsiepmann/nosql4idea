package org.apache.commons.httpclient

internal class HttpClientStub(private val responseStatus: Int, private val responseBody: String) : HttpClient() {

    var calledMethod: HttpMethod? = null

    override fun executeMethod(method: HttpMethod?): Int {
        calledMethod = method
        (method as HttpMethodBase).responseStream = responseBody.byteInputStream()
        return responseStatus
    }
}