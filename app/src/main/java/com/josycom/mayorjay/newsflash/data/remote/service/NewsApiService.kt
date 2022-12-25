package com.josycom.mayorjay.newsflash.data.remote.service

import com.josycom.mayorjay.newsflash.data.remote.model.NewsResponse
import com.josycom.mayorjay.newsflash.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET(Constants.TOP_HEADLINES_ENDPOINT)
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("sources") source: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NewsResponse
}