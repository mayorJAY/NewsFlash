package com.josycom.mayorjay.newsflash.data.mapper

import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.data.remote.model.ArticleRemote

fun ArticleRemote.toDomain(): Article {
    return Article(
        title = this.title ?: "",
        description = this.description ?: "",
        content = this.content ?: "",
        date = this.publishedAt ?: "",
        imageUrl = this.urlToImage ?: "",
    )
}