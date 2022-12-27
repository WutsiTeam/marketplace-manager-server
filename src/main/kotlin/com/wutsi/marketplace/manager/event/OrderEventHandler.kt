package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.event.OrderEventPayload
import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.access.dto.SearchReservationRequest
import com.wutsi.marketplace.manager.workflow.CancelReservationWorkflow
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.stream.Event
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class OrderEventHandler(
    private val mapper: ObjectMapper,
    private val logger: KVLogger,
    private val marketplaceAccessApi: MarketplaceAccessApi,
    private val cancelReservationWorkflow: CancelReservationWorkflow,
) {
    fun onOrderExpired(event: Event) {
        cancelReservation(event)
    }

    fun onOrderCancelled(event: Event) {
        cancelReservation(event)
    }

    private fun cancelReservation(event: Event) {
        val payload = toOrderEventPayload(event)
        log(payload)

        marketplaceAccessApi.searchReservation(
            request = SearchReservationRequest(
                orderId = payload.orderId,
            ),
        ).reservations.forEach {
            cancelReservationWorkflow.execute(it.id, WorkflowContext())
        }
    }

    private fun toOrderEventPayload(event: Event): OrderEventPayload =
        mapper.readValue(event.payload, OrderEventPayload::class.java)

    private fun log(payload: OrderEventPayload) {
        logger.add("payload_order_id", payload.orderId)
    }
}
