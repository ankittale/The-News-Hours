package com.ankitt.thenewshours.network

import com.ankitt.thenewshours.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNew(
        @Query("country")
        countryCode: String = "in",
        @Query("page")
        pageNumber: Int,
        @Query("apiKey")
        apiKey: String
    ): Response<NewsResponse>


    @GET("/v2/everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int,
        @Query("apiKey")
        apiKey: String
    ): Response<NewsResponse>
}