package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.enums.OrderStatus
import com.wutsi.event.OrderEventPayload
import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.access.dto.SearchReservationResponse
import com.wutsi.marketplace.access.dto.UpdateReservationStatusRequest
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.platform.core.stream.Event
import com.wutsi.platform.core.stream.EventStream
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.OffsetDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class OrderEventHandlerTest {
    @MockBean
    private lateinit var marketplaceAccessApi: MarketplaceAccessApi

    @MockBean
    private lateinit var eventStream: EventStream

    @Autowired
    private lateinit var handler: OrderEventHandler

    val orderId = "1111"
    val payload = OrderEventPayload(
        orderId = orderId
    )

    val event = Event(
        payload = ObjectMapper().writeValueAsString(payload),
        timestamp = OffsetDateTime.now()
    )

    @Test
    fun expire() {
        // GIVEN
        val reservations = listOf(
            Fixtures.createReservationSummary(1),
            Fixtures.createReservationSummary(2)
        )
        doReturn(SearchReservationResponse(reservations)).whenever(marketplaceAccessApi).searchReservation(any())

        // WHEN
        handler.onOrderExpired(event)

        // THEN
        verify(marketplaceAccessApi).updateReservationStatus(
            reservations[0].id,
            UpdateReservationStatusRequest(
                OrderStatus.EXPIRED.name
            )
        )
        verify(marketplaceAccessApi).updateReservationStatus(
            reservations[1].id,
            UpdateReservationStatusRequest(
                OrderStatus.EXPIRED.name
            )
        )

        verify(eventStream, never()).publish(any(), any())
    }
}
