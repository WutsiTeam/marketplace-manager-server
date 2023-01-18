package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.event.BusinessEventPayload
import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.access.dto.SearchStoreRequest
import com.wutsi.marketplace.manager.workflow.DeactivateStoreWorkflow
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.stream.Event
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class BusinessEventHandler(
    private val mapper: ObjectMapper,
    private val logger: KVLogger,
    private val marketplaceAccessApi: MarketplaceAccessApi,
    private val deactivateStoreWorkflow: DeactivateStoreWorkflow,
) {
    fun onBusinessDeactivated(event: Event) {
        val payload = toBusinessEventPayload(event)
        log(payload)

        val context = WorkflowContext(accountId = payload.accountId)
        marketplaceAccessApi.searchStore(
            request = SearchStoreRequest(
                businessId = payload.businessId,
            ),
        ).stores.forEach {
            logger.add("store_to_deactivate", it.id)
            deactivateStoreWorkflow.execute(it.id, context)
        }
    }

    private fun toBusinessEventPayload(event: Event): BusinessEventPayload =
        mapper.readValue(event.payload, BusinessEventPayload::class.java)

    private fun log(payload: BusinessEventPayload) {
        logger.add("payload_account_id", payload.accountId)
        logger.add("payload_business_id", payload.businessId)
    }
}
