package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.enums.ReservationStatus
import com.wutsi.enums.StoreStatus
import com.wutsi.event.BusinessEventPayload
import com.wutsi.event.EventURN
import com.wutsi.event.OrderEventPayload
import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.access.dto.SearchReservationResponse
import com.wutsi.marketplace.access.dto.SearchStoreResponse
import com.wutsi.marketplace.access.dto.UpdateReservationStatusRequest
import com.wutsi.marketplace.access.dto.UpdateStoreStatusRequest
import com.wutsi.marketplace.manager.Fixtures
import com.wutsi.membership.access.MembershipAccessApi
import com.wutsi.membership.access.dto.GetAccountResponse
import com.wutsi.platform.core.messaging.Message
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
import kotlin.test.assertEquals

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
    private lateinit var membershipAccessApi: MembershipAccessApi

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

    @Test
    fun onBusinessDeactivated() {
        // GIVEN
        val reservations = listOf(
            Fixtures.createReservationSummary(1),
            Fixtures.createReservationSummary(2),
        )
        doReturn(SearchReservationResponse(reservations)).whenever(marketplaceAccessApi).searchReservation(any())

        val account = Fixtures.createAccount(id = 111, storeId = 333L, businessId = 333)
        doReturn(GetAccountResponse(account)).whenever(membershipAccessApi).getAccount(any())

        val store = Fixtures.createStoreSummary(id = account.storeId!!, accountId = account.id)
        doReturn(SearchStoreResponse(listOf(store))).whenever(marketplaceAccessApi).searchStore(any())

        // WHEN
        val event = Event(
            type = EventURN.BUSINESS_DEACTIVATED.urn,
            payload = mapper.writeValueAsString(
                BusinessEventPayload(
                    accountId = account.id,
                    businessId = account.businessId!!,
                ),
            ),
        )
        handler.handleEvent(event)

        // THEN
        verify(marketplaceAccessApi).updateStoreStatus(
            account.storeId!!,
            UpdateStoreStatusRequest(StoreStatus.INACTIVE.name),
        )

        verify(eventStream).publish(
            EventURN.STORE_DEACTIVATED.urn,
            StoreEventPayload(storeId = store.id, accountId = store.accountId),
        )
    }

    @Test
    fun onWelcome() {
        // GIVEN
        val account = Fixtures.createAccount(
            id = 111,
            storeId = 222,
            displayName = "Yo Man",
            email = "yo-man@gmail.com",
            language = "fr",
        )
        doReturn(GetAccountResponse(account)).whenever(membershipAccessApi).getAccount(any())

        // WHEN
        val event = Event(
            type = InternalEventURN.WELCOME_TO_MERCHANT_SUBMITTED.urn,
            payload = mapper.writeValueAsString(
                StoreEventPayload(
                    accountId = account.id,
                    storeId = account.storeId!!,
                ),
            ),
        )
        handler.handleEvent(event)
        Thread.sleep(10000L)

        // THEN
        val msg = argumentCaptor<Message>()
        verify(messaging).send(msg.capture())
        assertEquals(account.email, msg.firstValue.recipient.email)
        assertEquals(account.displayName, msg.firstValue.recipient.displayName)
        assertEquals("Bienvenue dans la communauté Wutsi", msg.firstValue.subject)
    }
}
