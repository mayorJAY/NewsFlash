package com.josycom.mayorjay.newsflash.remote

import com.josycom.mayorjay.newsflash.data.remote.service.NewsApiService
import com.josycom.mayorjay.newsflash.util.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NewsApiServiceTest : TestCase() {

    private lateinit var service: NewsApiService
    private lateinit var server: MockWebServer

    override fun setUp() {
        server = MockWebServer()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NewsApiService::class.java)
    }

    fun `test getTopHeadlines request_sent valid_path_called`() {
        runBlocking {
            enqueueMockResponse("BbcNewsSuccessResponse.json")
            service.getTopHeadlines("", "bbc-news", 10, 1)
            val request = server.takeRequest()
            assertTrue((request.path ?: "").contains(Constants.TOP_HEADLINES_ENDPOINT))
        }
    }

    fun `test getTopHeadlines request_sent valid_response_code_received`() {
        runBlocking {
            enqueueMockResponse("CnnNewsSuccessResponse.json")
            val responseBody = service.getTopHeadlines("", "cnn", 10, 1)
            assertEquals("ok", responseBody.status)
        }
    }

    fun `test getTopHeadlines request_sent valid_response_body_received`() {
        runBlocking {
            enqueueMockResponse("BbcNewsSuccessResponse.json")
            val responseBody = service.getTopHeadlines("", "bbc-news", 10, 1)
            assertNotNull(responseBody)
        }
    }

    fun `test getTopHeadlines request_sent valid_source_name_received_from_response`() {
        runBlocking {
            enqueueMockResponse("CnnNewsSuccessResponse.json")
            val responseBody = service.getTopHeadlines("", "cnn", 10, 1)
            val firstItem = (responseBody.articles ?: emptyList())[0]
            assertEquals("CNN", firstItem.source?.name)
        }
    }

    fun `test getTopHeadlines request_sent valid_source_id_received_from_response`() {
        runBlocking {
            enqueueMockResponse("BbcNewsSuccessResponse.json")
            val responseBody = service.getTopHeadlines("", "bbc-news", 10, 1)
            val firstItem = (responseBody.articles ?: emptyList())[0]
            assertEquals("bbc-news", firstItem.source?.id)
        }
    }

    fun `test getTopHeadlines request_sent error_code_received_from_response`() {
        runBlocking {
            enqueueMockResponse("NewsApiFailureResponse.json")
            val responseBody = service.getTopHeadlines("", "bbc-news", 10, 1)
            assertEquals("error", responseBody.status)
        }
    }

    fun `test getTopHeadlines request_sent error_message_received_from_response`() {
        runBlocking {
            enqueueMockResponse("NewsApiFailureResponse.json")
            val responseBody = service.getTopHeadlines("", "bbc-news", 10, 1)
            assertNotNull(responseBody.message)
        }
    }

    private fun enqueueMockResponse(fileName: String) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    override fun tearDown() {
        server.shutdown()
    }
}