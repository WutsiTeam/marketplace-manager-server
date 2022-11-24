package com.wutsi.marketplace.manager.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.marketplace.access.dto.GetProductResponse
import com.wutsi.marketplace.access.dto.UpdateProductStatusRequest
import com.wutsi.marketplace.access.enums.ProductStatus
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.workflow.error.ErrorURN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PublishProductControllerTest : AbstractProductControllerTest<Long>() {
    override fun url() = "http://localhost:$port/v1/products/$PRODUCT_ID/publish"
    override fun createRequest(): Long? = null

    @Test
    fun publish() {
        // WHEN
        val response = rest.postForEntity(url(), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).updateProductStatus(
            PRODUCT_ID,
            UpdateProductStatusRequest(
                status = ProductStatus.PUBLISHED.name
            )
        )

        verify(eventStream, never()).publish(any(), any())
    }

    @Test
    fun infiniteStorck() {
        // GIVEN
        product = Fixtures.createProduct(
            id = PRODUCT_ID,
            storeId = STORE_ID,
            quantity = null,
            pictures = listOf(Fixtures.createPictureSummary(1), Fixtures.createPictureSummary(2))
        )
        doReturn(GetProductResponse(product)).whenever(marketplaceAccessApi).getProduct(any())

        // WHEN
        val response = rest.postForEntity(url(), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).updateProductStatus(
            PRODUCT_ID,
            UpdateProductStatusRequest(
                status = ProductStatus.PUBLISHED.name
            )
        )

        verify(eventStream, never()).publish(any(), any())
    }

    @Test
    fun noStock() {
        // GIVEN
        product = Fixtures.createProduct(
            id = PRODUCT_ID,
            storeId = STORE_ID,
            quantity = 0,
            pictures = listOf(Fixtures.createPictureSummary(1), Fixtures.createPictureSummary(2))
        )
        doReturn(GetProductResponse(product)).whenever(marketplaceAccessApi).getProduct(any())

        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(), request, Any::class.java)
        }
        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_NO_STOCK.urn, response.error.code)

        verify(marketplaceAccessApi, never()).updateProductStatus(any(), any())
        verify(eventStream, never()).publish(any(), any())
    }

    @Test
    fun noPictures() {
        // GIVEN
        product = Fixtures.createProduct(
            id = PRODUCT_ID,
            storeId = STORE_ID,
            pictures = emptyList()
        )
        doReturn(GetProductResponse(product)).whenever(marketplaceAccessApi).getProduct(any())

        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(), request, Any::class.java)
        }
        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_PICTURE_MISSING.urn, response.error.code)

        verify(marketplaceAccessApi, never()).updateProductStatus(any(), any())
        verify(eventStream, never()).publish(any(), any())
    }
}