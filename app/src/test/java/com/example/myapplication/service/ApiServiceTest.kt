package com.example.myapplication.service

import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

import org.junit.Assert.assertEquals

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class ApiServiceTest {

    @Test
    fun `test getRatesResponse success`() = runBlocking {
        val mockClient = mock(OkHttpClient::class.java)
        val mockCall = mock(Call::class.java)
        val mockResponse = mock(Response::class.java)
        val mockResponseBody = mock(ResponseBody::class.java)

        val json = "{\"base\":\"EUR\",\"date\":\"2024-02-29\",\"rates\":{\"AED\":4.147043,\"AFN\":118.466773,\"ALL\":120.73174}}"

        `when`(mockClient.newCall(mock(Request::class.java))).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body).thenReturn(mockResponseBody)
        `when`(mockResponseBody.string()).thenReturn(json)

        val apiService = ApiService()

        `when`(mockClient.newBuilder()).thenReturn(mock(OkHttpClient.Builder::class.java))
        `when`(mockClient.newBuilder().build()).thenReturn(mockClient)

        val result = apiService.getRatesResponse("https://developers.paysera.com/tasks/api/currency-exchange-rates")

        assertEquals("EUR", result?.base)
        assertEquals("2024-02-29", result?.date)
        assertEquals(4.147043, result?.rates?.get("AED"))
        assertEquals(118.466773, result?.rates?.get("AFN"))
        assertEquals(120.73174, result?.rates?.get("ALL"))
    }
}
