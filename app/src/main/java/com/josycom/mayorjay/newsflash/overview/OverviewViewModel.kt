package com.josycom.mayorjay.newsflash.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(repository: NewsRepository) : ViewModel() {

    private var newsPagingFlow: Flow<PagingData<Article>> = flowOf()
    fun getNewsPagingFlow(): Flow<PagingData<Article>> = newsPagingFlow
    private fun setNewsPagingFlow(newsPagingFlow: Flow<PagingData<Article>>) { this.newsPagingFlow = newsPagingFlow }

    init {
        val newsPagingFlow = repository.fetchNewsArticleFlow().cachedIn(viewModelScope)
        setNewsPagingFlow(newsPagingFlow)
    }
}