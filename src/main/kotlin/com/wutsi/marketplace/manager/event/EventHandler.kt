package com.wutsi.marketplace.manager.event

import com.wutsi.event.EventURN
import com.wutsi.platform.core.stream.Event
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventHandler(
    private val business: BusinessEventHandler,
    private val order: OrderEventHandler,
) {
    @EventListener
    fun handleEvent(event: Event) {
        when (event.type) {
            EventURN.ORDER_EXPIRED.urn -> order.onOrderExpired(event)
            EventURN.BUSINESS_DEACTIVATED.urn -> business.onBusinessDeactivated(event)
            else -> {}
        }
    }
}
