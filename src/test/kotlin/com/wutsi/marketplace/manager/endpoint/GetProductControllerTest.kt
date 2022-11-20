package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.marketplace.access.dto.GetProductResponse
import com.wutsi.marketplace.manager.Fixtures
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetProductControllerTest : AbstractProductControllerTest<Long>() {
    override fun url() = "http://localhost:$port/v1/products/$PRODUCT_ID"

    override fun createRequest(): Long? = PRODUCT_ID

    override fun submit() {
        rest.getForEntity(url(), com.wutsi.marketplace.manager.dto.GetProductResponse::class.java)
    }

    @Test
    public fun invoke() {
        // GIVEN
        val product = Fixtures.createProduct(id = PRODUCT_ID, storeId = STORE_ID)
        doReturn(GetProductResponse(product)).whenever(marketplaceAccessApi).getProduct(any())

        // WHEN
        val response =
            rest.getForEntity(url(), com.wutsi.marketplace.manager.dto.GetProductResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).getProduct(PRODUCT_ID)

        val prod = response.body!!.product
        assertEquals(product.id, prod.id)
        assertEquals(product.storeId, prod.storeId)
        assertEquals(product.title, prod.title)
        assertEquals(product.summary, prod.summary)
        assertEquals(product.description, prod.description)
        assertEquals(product.price, prod.price)
        assertEquals(product.comparablePrice, prod.comparablePrice)
        assertEquals(product.currency, prod.currency)
        assertEquals(product.thumbnail?.url, prod.thumbnail?.url)
        assertEquals(product.pictures.size, prod.pictures.size)

        verify(eventStream, never()).publish(any(), any())
    }

    @Test
    override fun notProductOwner() {
        // NOTHING
    }
}
