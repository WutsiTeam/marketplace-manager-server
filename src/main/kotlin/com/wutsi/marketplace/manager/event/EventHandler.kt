package com.wutsi.marketplace.manager.event

import com.wutsi.event.EventURN
import com.wutsi.platform.core.stream.Event
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventHandler(
    private val business: BusinessEventHandler,
    private val order: OrderEventHandler,
    private val store: StoreEventHandler,
) {
    @EventListener
    fun handleEvent(event: Event) {
        when (event.type) {
            EventURN.ORDER_EXPIRED.urn -> order.onOrderExpired(event)
            EventURN.ORDER_CANCELLED.urn -> order.onOrderExpired(event)
            EventURN.BUSINESS_DEACTIVATED.urn -> business.onBusinessDeactivated(event)
            InternalEventURN.WELCOME_TO_MERCHANT_SUBMITTED.urn -> store.onWelcome(event)
            else -> {}
        }
    }
}
