package com.example.mypersonalassistant

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WolframAlphaResponse(
    @Json(name = "queryresult")
    val queryResult: QueryResult
)

@JsonClass(generateAdapter = true)
data class QueryResult(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "pods")
    val pods: List<Pod>?
)

@JsonClass(generateAdapter = true)
data class Pod(
    @Json(name = "title")
    val title: String,
    @Json(name = "subpods")
    val subpods: List<Subpod>
)

@JsonClass(generateAdapter = true)
data class Subpod(
    @Json(name = "title")
    val title: String,
    @Json(name = "plaintext")
    val plaintext: String
)
