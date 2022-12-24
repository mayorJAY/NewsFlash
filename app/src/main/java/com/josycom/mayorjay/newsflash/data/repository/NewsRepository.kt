package com.josycom.mayorjay.newsflash.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.josycom.mayorjay.newsflash.BuildConfig
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.data.remote.datasource.NewsPagingSource
import com.josycom.mayorjay.newsflash.data.remote.service.NewsApiService
import com.josycom.mayorjay.newsflash.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val apiService: NewsApiService) {

    fun fetchNewsArticleFlow(): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            enablePlaceholders = false
        ),
            pagingSourceFactory = {
                NewsPagingSource(
                    BuildConfig.SOURCE_ID,
                    BuildConfig.API_KEY,
                    apiService,
                    Constants.FIRST_PAGE,
                    Constants.PAGE_SIZE
                )
            }).flow
    }
}