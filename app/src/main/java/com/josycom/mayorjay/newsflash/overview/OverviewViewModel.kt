package com.josycom.mayorjay.newsflash.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.josycom.mayorjay.newsflash.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(repository: NewsRepository) : ViewModel() {

    val newsPagingFlow = repository.fetchNewsArticleFlow().cachedIn(viewModelScope)
}