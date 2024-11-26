package com.example.mypersonalassistant

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WolframAlphaApi {
    @GET("v2/query")
    suspend fun getAnswer(
        @Query("input") input: String,
        @Query("appid") appId: String,
        @Query("format") format: String = "plaintext"
    ): Response<WolframAlphaResponse> // Change from WolframAlphaResponse to Response<WolframAlphaResponse>

    companion object {
        fun create(): WolframAlphaApi {
            return NetworkModule.createRetrofit().create(WolframAlphaApi::class.java)
        }
    }
}
