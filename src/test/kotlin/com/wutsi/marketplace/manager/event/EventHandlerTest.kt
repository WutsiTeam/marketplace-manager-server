package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.verify
import com.wutsi.event.EventURN
import com.wutsi.event.OrderEventPayload
import com.wutsi.platform.core.stream.Event
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class EventHandlerTest {
    @MockBean
    private lateinit var order: OrderEventHandler

    @Autowired
    private lateinit var handler: EventHandler

    @Autowired
    private lateinit var mapper: ObjectMapper

    private val orderEventPayload = OrderEventPayload(
        orderId = "1111"
    )

    @Test
    fun onOrderExpired() {
        // WHEN
        val event = Event(
            type = EventURN.ORDER_EXPIRED.urn,
            payload = mapper.writeValueAsString(orderEventPayload)
        )
        handler.handleEvent(event)

        // THEN
        verify(order).onOrderExpired(event)
    }
}
