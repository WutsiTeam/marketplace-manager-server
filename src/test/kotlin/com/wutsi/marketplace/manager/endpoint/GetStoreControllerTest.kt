package com.wutsi.marketplace.manager.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.marketplace.access.dto.GetStoreResponse
import com.wutsi.marketplace.access.enums.StoreStatus
import com.wutsi.marketplace.manager.Fixtures
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetStoreControllerTest : AbstractStoreControllerTest<Long>() {
    override fun url() = "http://localhost:$port/v1/stores/$STORE_ID"

    override fun createRequest(): Long? = STORE_ID

    override fun submit() {
        rest.getForEntity(url(), com.wutsi.marketplace.manager.dto.GetStoreResponse::class.java)
    }

    @Test
    public fun invoke() {
        // GIVEN
        val store = Fixtures.createStore(id = STORE_ID, accountId = ACCOUNT_ID, status = StoreStatus.ACTIVE)
        doReturn(GetStoreResponse(store)).whenever(marketplaceAccessApi).getStore(any())

        // WHEN
        val response =
            rest.getForEntity(url(), com.wutsi.marketplace.manager.dto.GetStoreResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        verify(marketplaceAccessApi).getStore(STORE_ID)

        val prod = response.body!!.store
        assertEquals(store.id, prod.id)
        assertEquals(store.accountId, prod.accountId)
        assertEquals(store.status, prod.status)

        verify(eventStream, never()).publish(any(), any())
    }
}
