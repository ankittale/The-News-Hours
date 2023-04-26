package com.ankitt.thenewshours.repository

import com.ankitt.thenewshours.db.ArticleDatabase
import com.ankitt.thenewshours.model.Article
import com.ankitt.thenewshours.network.NewsInstance

class NewsRepository(val database: ArticleDatabase) {

    //Suspend Function Because News API have same calling style
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int, apiKey: String) =
        NewsInstance.api.getBreakingNew(countryCode, pageNumber, apiKey)

    suspend fun searchNews(searchQuery: String, pageNumber: Int, apiKey: String) =
        NewsInstance.api.searchNews(searchQuery, pageNumber, apiKey)

    suspend fun insertNewsToDB(article: Article) = database.getArticleDao().upsert(article)

    fun savedNews() = database.getArticleDao().getAllArticles()

    suspend fun deleteArticleFromDB(article: Article) =
        database.getArticleDao().deleteArticle(article)
}