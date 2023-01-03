package com.josycom.mayorjay.newsflash.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.data.mapper.toDomain
import com.josycom.mayorjay.newsflash.data.remote.service.NewsApiService
import com.josycom.mayorjay.newsflash.util.Constants
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val apiService: NewsApiService,
    private val apiKey: String,
    private val sourceId: String,
    private val pageSize: Int,
    private val firstPage: Int
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
                apiKey,
                sourceId,
                pageSize,
                page
            )
            if (Constants.OK.equals(response.status, true)) {
                val articles = (response.articles ?: emptyList()).map { articleRemote -> articleRemote.toDomain() }
                val nextKey = if (articles.isEmpty()) null else page + (params.loadSize / pageSize)
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == firstPage) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Throwable(message = response.message))
            }
        } catch (exception: IOException) {
            if (exception is UnknownHostException) {
                return LoadResult.Error(Throwable(message = "Something is not right! Please check your network"))
            }
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            if (exception.response()?.code() == 401) {
                return LoadResult.Error(Throwable(message = "Your API key is invalid or incorrect. Check your key, or go to https://newsapi.org to create a free API key"))
            }
            LoadResult.Error(exception)
        }
    }
}