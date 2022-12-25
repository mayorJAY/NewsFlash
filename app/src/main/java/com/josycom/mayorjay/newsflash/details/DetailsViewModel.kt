package com.josycom.mayorjay.newsflash.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.newsflash.data.domain.Article

class DetailsViewModel : ViewModel() {

    private var article: MutableLiveData<Article> = MutableLiveData()
    fun getArticle(): MutableLiveData<Article> = article
    fun setArticle(article: Article?){ this.article.value = article }
}