package com.josycom.mayorjay.newsflash.data.remote.model

data class NewsResponse(
    val articles: List<ArticleRemote>?,
    val status: String?,
    val code: String?,
    val message: String?,
    val totalResults: Int?
)

data class ArticleRemote(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)

data class Source(
    val id: String?,
    val name: String?
)
