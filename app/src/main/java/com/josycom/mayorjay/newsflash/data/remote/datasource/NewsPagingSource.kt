package com.josycom.mayorjay.newsflash.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.data.mapper.toDomain
import com.josycom.mayorjay.newsflash.data.remote.service.NewsApiService
import com.josycom.mayorjay.newsflash.util.Constants
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val sourceId: String,
    private val apiKey: String,
    private val apiService: NewsApiService,
    private val firstPage: Int,
    private val pageSize: Int
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: firstPage
        return try {
            val response = apiService.getTopHeadlines(
                sourceId,
                apiKey,
                pageSize,
                page
            )
            if (Constants.OK.equals(response.status, true)) {
                val articles = response.articles.map { articleRemote -> articleRemote.toDomain() }
                val nextKey = if (articles.isEmpty()) {
                    null
                } else {
                    page + (params.loadSize / pageSize)
                }
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == firstPage) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Throwable(message = response.message))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}