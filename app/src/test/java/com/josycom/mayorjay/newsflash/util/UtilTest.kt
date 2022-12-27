package com.josycom.mayorjay.newsflash.util

import junit.framework.TestCase

class UtilTest : TestCase() {

    fun `test getModifiedNewsSource news_appended_to_source_without_the_word`() {
        val result = "The Guardian".getModifiedNewsSource("News")
        assertEquals("The Guardian News", result)
    }

    fun `test getModifiedNewsSource source_returned_unmodified`() {
        val result = "BBC News".getModifiedNewsSource("News")
        assertEquals("BBC News", result)
    }

    fun `test getFormattedDate yyyy-MM-ddTHH_mm_ssZ_date_format parsed_successfully`() {
        val result = "2022-12-22T15:28:00Z".getFormattedDate()
        assertTrue(result.contains("Dec"))
    }

    fun `test getFormattedDate yyyy-MM-ddTHH_mm_ss_SSSSSSSZ_date_format parsed_successfully`() {
        val result = "2022-12-22T19:22:22.7482529Z".getFormattedDate()
        assertTrue(result.contains("Dec"))
    }

    fun `test getFormattedDate yyyy-MM-ddTHH_mm_ss_SSSZ_date_format parsed_successfully`() {
        val result = "2022-12-22T19:22:22.748Z".getFormattedDate()
        assertTrue(result.contains("Dec"))
    }

    fun `test getFormattedDate any_other_date_format parsing_failed`() {
        val result = "2022-12-22T19:57:54+00:00".getFormattedDate()
        assertFalse(result.contains("Dec"))
    }
}