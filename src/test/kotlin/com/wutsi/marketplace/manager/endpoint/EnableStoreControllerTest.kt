package com.wutsi.marketplace.manager.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.error.ErrorURN
import com.wutsi.event.EventURN
import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.access.dto.CreateStoreRequest
import com.wutsi.marketplace.access.dto.CreateStoreResponse
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.marketplace.manager.dto.EnableStoreResponse
import com.wutsi.membership.access.dto.GetAccountResponse
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EnableStoreControllerTest : AbstractStoreControllerTest<Void>() {
    override fun url() = "http://localhost:$port/v1/stores"

    override fun createRequest(): Void? = null

    @Test
    fun enable() {
        // GIVEN
        doReturn(CreateStoreResponse(STORE_ID)).whenever(marketplaceAccessApi).createStore(any())

        // WHEN
        val response = rest.postForEntity(url(), null, EnableStoreResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).createStore(
            CreateStoreRequest(
                accountId = account.id,
                currency = "XAF"
            )
        )

        verify(eventStream).publish(
            EventURN.STORE_ENABLED.urn,
            StoreEventPayload(
                accountId = account.id,
                storeId = STORE_ID
            )
        )
    }

    @Test
    fun countryNotSupported() {
        // GIVEN
        val account = Fixtures.createAccount(country = "CA", business = true)
        doReturn(GetAccountResponse(account)).whenever(membershipAccessApi).getAccount(any())

        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(), null, EnableStoreResponse::class.java)
        }
        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.STORE_NOT_SUPPORTED_IN_COUNTRY.urn, response.error.code)

        verify(marketplaceAccessApi, never()).createStore(any())
        verify(eventStream, never()).publish(any(), any())
    }
}
