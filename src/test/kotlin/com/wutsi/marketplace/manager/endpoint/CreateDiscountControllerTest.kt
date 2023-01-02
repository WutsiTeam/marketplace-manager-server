package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.marketplace.manager.dto.CreateDiscountRequest
import com.wutsi.marketplace.manager.dto.CreateDiscountResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.time.LocalDate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateDiscountControllerTest : AbstractDiscountControllerTest() {
    @Test
    public fun invoke() {
        // GIVEN
        val discountId = 11L
        doReturn(com.wutsi.marketplace.access.dto.CreateDiscountResponse(discountId)).whenever(marketplaceAccessApi)
            .createDiscount(
                any(),
            )

        // WHEN
        val request = CreateDiscountRequest(
            name = "FIN10",
            rate = 10,
            starts = LocalDate.now(),
            ends = LocalDate.now().plusDays(10),
            allProducts = true,
        )
        val response = rest.postForEntity(url(), request, CreateDiscountResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).createDiscount(
            com.wutsi.marketplace.access.dto.CreateDiscountRequest(
                storeId = account.storeId!!,
                name = request.name,
                rate = request.rate,
                starts = request.starts,
                ends = request.ends,
                allProducts = request.allProducts,
            ),
        )
    }

    private fun url() = "http://localhost:$port/v1/discounts"
}
