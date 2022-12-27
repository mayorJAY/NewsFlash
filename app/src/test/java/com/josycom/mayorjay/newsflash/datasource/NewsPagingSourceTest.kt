package com.josycom.mayorjay.newsflash.datasource

import androidx.paging.PagingSource
import com.josycom.mayorjay.newsflash.data.mapper.toDomain
import com.josycom.mayorjay.newsflash.data.remote.datasource.NewsPagingSource
import com.josycom.mayorjay.newsflash.data.remote.model.ArticleRemote
import com.josycom.mayorjay.newsflash.data.remote.model.NewsResponse
import com.josycom.mayorjay.newsflash.data.remote.model.Source
import com.josycom.mayorjay.newsflash.data.remote.service.NewsApiService
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking

class NewsPagingSourceTest : TestCase() {

    fun `test load success_status_returned_with_null_articles actual_result_has_empty_list`() = runBlocking {
        val service = getApiService(null, "Ok", "")
        val pagingSource = NewsPagingSource(service, "", "", 10, 1)
        val expected = PagingSource.LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        val actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals(expected, actual)
    }

    fun `test load success_status_returned_with_valid_articles actual_result_has_valid_list`() = runBlocking {
        val articles = listOf(ArticleRemote("", "", "", "", Source("", ""), "", "", ""))
        val service = getApiService(articles, "Ok", "")
        val pagingSource = NewsPagingSource(service, "", "", 10, 1)
        val expected = PagingSource.LoadResult.Page(data = articles.map { it.toDomain() }, prevKey = null, nextKey = 2)
        val actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals(expected, actual)
    }

    fun `test load success_status_returned_with_valid_articles actual_result_has_valid_item`() = runBlocking {
        val articles = listOf(ArticleRemote("", "", "", "", Source("", ""), "Breaking News", "", ""))
        val service = getApiService(articles, "Ok", "")
        val pagingSource = NewsPagingSource(service, "", "", 10, 1)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals("Breaking News", (result as PagingSource.LoadResult.Page).data[0].title)
    }

    fun `test load failure_status_returned error_load_result_propagated`() = runBlocking {
        val service = getApiService(null, "Error", "")
        val pagingSource = NewsPagingSource(service, "", "", 10, 1)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertTrue(result is PagingSource.LoadResult.Error)
    }

    fun `test load failure_status_returned error_message_propagated`() = runBlocking {
        val service = getApiService(null, "Error", "Error reaching Remote server")
        val pagingSource = NewsPagingSource(service, "", "", 10, 1)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 10, false))
        assertEquals("Error reaching Remote server", (result as PagingSource.LoadResult.Error).throwable.message)
    }

    private fun getApiService(articles: List<ArticleRemote>?, status: String?, message: String?): NewsApiService {
        return object: NewsApiService {
            override suspend fun getTopHeadlines(
                apiKey: String,
                source: String,
                pageSize: Int,
                page: Int
            ): NewsResponse {
                return NewsResponse(articles, status, null, message, pageSize)
            }
        }
    }
}