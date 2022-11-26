package com.wutsi.marketplace.manager.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.enums.StoreStatus
import com.wutsi.event.EventURN
import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.access.dto.UpdateStoreStatusRequest
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.membership.access.dto.GetAccountResponse
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.workflow.error.ErrorURN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SuspendStoreControllerTest : AbstractStoreControllerTest<Void>() {
    override fun url() = "http://localhost:$port/v1/stores"

    override fun createRequest(): Void? = null

    @Test
    fun disable() {
        // GIVEN
        val account = Fixtures.createAccount(id = ACCOUNT_ID, business = true, storeId = STORE_ID)
        doReturn(GetAccountResponse(account)).whenever(membershipAccessApi).getAccount(any())

        // WHEN
        rest.delete(url())

        // THEN
        verify(marketplaceAccessApi).updateStoreStatus(
            account.storeId!!,
            UpdateStoreStatusRequest(
                status = StoreStatus.SUSPENDED.name
            )
        )

        verify(eventStream).publish(
            EventURN.STORE_SUSPENDED.urn,
            StoreEventPayload(
                accountId = account.id,
                storeId = STORE_ID
            )
        )
    }

    @Test
    fun noStore() {
        // GIVEN
        val account = Fixtures.createAccount(id = ACCOUNT_ID, business = true, storeId = null)
        doReturn(GetAccountResponse(account)).whenever(membershipAccessApi).getAccount(any())

        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            rest.delete(url())
        }

        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.NO_STORE.urn, response.error.code)

        verify(marketplaceAccessApi, never()).createStore(any())
        verify(eventStream, never()).publish(any(), any())
    }
}
