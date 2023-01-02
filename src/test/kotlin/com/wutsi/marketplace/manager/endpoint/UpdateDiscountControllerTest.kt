package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.verify
import com.wutsi.marketplace.manager.dto.UpdateDiscountRequest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.time.LocalDate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateDiscountControllerTest : AbstractDiscountControllerTest() {
    @Test
    public fun invoke() {
        // WHEN
        val request = UpdateDiscountRequest(
            name = "FIN10",
            rate = 10,
            starts = LocalDate.now(),
            ends = LocalDate.now().plusDays(10),
            allProducts = true,
        )
        val response = rest.postForEntity(url(100L), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).updateDiscount(
            100L,
            com.wutsi.marketplace.access.dto.UpdateDiscountRequest(
                name = request.name,
                rate = request.rate,
                starts = request.starts,
                ends = request.ends,
                allProducts = request.allProducts,
            ),
        )
    }

    private fun url(id: Long) = "http://localhost:$port/v1/discounts/$id"
}
