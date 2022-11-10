package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImportCategoryControllerTest : AbstractControllerTest() {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun en() {
        val response = rest.getForEntity(url("en"), Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        Thread.sleep(10000)
        verify(marketplaceAccessApi, times(5595)).saveCategory(any(), any())
    }

    @Test
    fun fr() {
        val response = rest.getForEntity(url("fr"), Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        Thread.sleep(10000)
        verify(marketplaceAccessApi, times(5595)).saveCategory(any(), any())
    }

    private fun url(language: String) = "http://localhost:$port/v1/categories/import?language=$language"
}
