package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.wutsi.marketplace.access.enums.ProductStatus
import com.wutsi.marketplace.manager.dto.UpdateProductStatusRequest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateProductStatusControllerTest : AbstractProductControllerTest<UpdateProductStatusRequest>() {
    override fun url() = "http://localhost:$port/v1/products/status"

    override fun createRequest() = UpdateProductStatusRequest(
        productId = PRODUCT_ID,
        status = ProductStatus.PUBLISHED.name
    )

    @Test
    fun update() {
        // WHEN
        val response = rest.postForEntity(url(), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).updateProductStatus(
            request!!.productId,
            com.wutsi.marketplace.access.dto.UpdateProductStatusRequest(
                status = request!!.status
            )
        )

        verify(eventStream, never()).publish(any(), any())
    }
}
