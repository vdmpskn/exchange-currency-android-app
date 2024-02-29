package com.example.myapplication.service

import android.util.Log
import com.example.myapplication.model.RatesResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class ApiService() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getRatesResponse(apiUrl: String): RatesResponse? {
        return withContext(dispatcher) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(apiUrl)
                    .build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val content = response.body?.string()
                    val gson = Gson()
                    val ratesResponse = gson.fromJson(content, RatesResponse::class.java)

                    if (ratesResponse != null) {
                        return@withContext ratesResponse
                    } else {
                        Log.d("Exchange", "RatesResponse is null")
                    }
                } else {
                    Log.d("Exchange", "Failed to fetch data. Response code: ${response.code}")
                }

                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}