package com.ankitt.thenewshours.repository

import com.ankitt.thenewshours.db.ArticleDatabase
import com.ankitt.thenewshours.network.NewsInstance

class NewsRepository(val database: ArticleDatabase) {

    //Suspend Function Because News API have same calling style
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int, apiKey: String) =
        NewsInstance.api.getBreakingNew(countryCode, pageNumber, apiKey)

    suspend fun searchNews(searchQuery: String, pageNumber: Int, apiKey: String) =
        NewsInstance.api.searchNews(searchQuery, pageNumber, apiKey)
}