package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.enums.ReservationStatus
import com.wutsi.event.EventURN
import com.wutsi.event.OrderEventPayload
import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.access.dto.SearchReservationResponse
import com.wutsi.marketplace.access.dto.UpdateReservationStatusRequest
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.platform.core.messaging.MessagingService
import com.wutsi.platform.core.messaging.MessagingServiceProvider
import com.wutsi.platform.core.messaging.MessagingType
import com.wutsi.platform.core.stream.Event
import com.wutsi.platform.core.stream.EventStream
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class EventHandlerTest {
    @MockBean
    private lateinit var eventStream: EventStream

    @Autowired
    private lateinit var handler: EventHandler

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    private lateinit var marketplaceAccessApi: MarketplaceAccessApi

    @MockBean
    private lateinit var messagingServiceProvider: MessagingServiceProvider

    private lateinit var messaging: MessagingService

    @BeforeEach
    fun setUp() {
        messaging = mock()
        doReturn(messaging).whenever(messagingServiceProvider).get(MessagingType.EMAIL)
    }

    @Test
    fun onOrderExpired() {
        // GIVEN
        val reservations = listOf(
            Fixtures.createReservationSummary(1),
            Fixtures.createReservationSummary(2),
        )
        doReturn(SearchReservationResponse(reservations)).whenever(marketplaceAccessApi).searchReservation(any())

        // WHEN
        val event = Event(
            type = EventURN.ORDER_EXPIRED.urn,
            payload = mapper.writeValueAsString(
                OrderEventPayload(
                    orderId = "1111",
                ),
            ),
        )
        handler.handleEvent(event)

        // THEN
        verify(marketplaceAccessApi).updateReservationStatus(
            reservations[0].id,
            UpdateReservationStatusRequest(
                ReservationStatus.CANCELLED.name,
            ),
        )
        verify(marketplaceAccessApi).updateReservationStatus(
            reservations[1].id,
            UpdateReservationStatusRequest(
                ReservationStatus.CANCELLED.name,
            ),
        )

        verify(eventStream, never()).publish(any(), any())
    }

    @Test
    fun onOrderCancelled() {
        // GIVEN
        val reservations = listOf(
            Fixtures.createReservationSummary(1),
            Fixtures.createReservationSummary(2),
        )
        doReturn(SearchReservationResponse(reservations)).whenever(marketplaceAccessApi).searchReservation(any())

        // WHEN
        val event = Event(
            type = EventURN.ORDER_CANCELLED.urn,
            payload = mapper.writeValueAsString(
                OrderEventPayload(
                    orderId = "1111",
                ),
            ),
        )
        handler.handleEvent(event)

        // THEN
        verify(marketplaceAccessApi).updateReservationStatus(
            reservations[0].id,
            UpdateReservationStatusRequest(
                ReservationStatus.CANCELLED.name,
            ),
        )
        verify(marketplaceAccessApi).updateReservationStatus(
            reservations[1].id,
            UpdateReservationStatusRequest(
                ReservationStatus.CANCELLED.name,
            ),
        )

        verify(eventStream, never()).publish(any(), any())
    }
}
