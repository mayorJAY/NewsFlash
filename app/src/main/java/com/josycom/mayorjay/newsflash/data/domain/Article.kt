package com.josycom.mayorjay.newsflash.data.domain

import java.io.Serializable

data class Article(
    val title: String,
    val description: String,
    val content: String,
    val date: String,
    val url: String,
    val imageUrl: String
) : Serializable
