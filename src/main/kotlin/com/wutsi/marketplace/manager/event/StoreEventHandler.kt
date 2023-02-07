package com.wutsi.marketplace.manager.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.manager.workflow.WelcomeEmailWorkflow
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.stream.Event
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class StoreEventHandler(
    private val mapper: ObjectMapper,
    private val logger: KVLogger,
    private val welcomeEmailWorkflow: WelcomeEmailWorkflow,
) {
    fun onWelcome(event: Event) {
        val payload = toStoreEventPayload(event)
        log(payload)

        welcomeEmailWorkflow.execute(payload.accountId, WorkflowContext(accountId = payload.accountId))
    }

    private fun toStoreEventPayload(event: Event): StoreEventPayload =
        mapper.readValue(event.payload, StoreEventPayload::class.java)

    private fun log(payload: StoreEventPayload) {
        logger.add("payload_account_id", payload.accountId)
        logger.add("payload_store_id", payload.storeId)
    }
}
