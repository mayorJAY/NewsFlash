package com.josycom.mayorjay.newsflash.data.mapper

import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.data.remote.model.ArticleRemote
import com.josycom.mayorjay.newsflash.util.getFormattedDate

fun ArticleRemote.toDomain(): Article {
    return Article(
        title = this.title ?: "",
        description = this.description ?: "",
        content = this.content ?: "",
        date = (this.publishedAt ?: "").getFormattedDate(),
        url = this.url ?: "",
        imageUrl = this.urlToImage ?: "",
    )
}