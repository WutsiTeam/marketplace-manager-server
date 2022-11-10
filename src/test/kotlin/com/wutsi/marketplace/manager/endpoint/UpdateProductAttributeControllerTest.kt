package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.wutsi.marketplace.manager.dto.UpdateProductAttributeRequest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateProductAttributeControllerTest : AbstractProductControllerTest<UpdateProductAttributeRequest>() {
    override fun url() = "http://localhost:$port/v1/products/attributes"

    override fun createRequest() = UpdateProductAttributeRequest(
        productId = PRODUCT_ID,
        name = "title",
        value = "ello world"
    )

    @Test
    fun update() {
        // WHEN
        val response =
            rest.postForEntity(url(), request, com.wutsi.marketplace.manager.dto.CreateProductResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).updateProductAttribute(
            request!!.productId,
            com.wutsi.marketplace.access.dto.UpdateProductAttributeRequest(
                name = request!!.name,
                value = request!!.value
            )
        )

        verify(eventStream, never()).publish(any(), any())
    }
}
