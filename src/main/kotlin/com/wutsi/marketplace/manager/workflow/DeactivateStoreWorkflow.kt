package com.wutsi.marketplace.manager.workflow

import com.wutsi.enums.StoreStatus
import com.wutsi.event.EventURN
import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.access.dto.UpdateStoreStatusRequest
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class DeactivateStoreWorkflow(
    eventStream: EventStream,
) : AbstractStoreWorkflow<Long, Unit>(eventStream) {
    override fun getEventType(storeId: Long, response: Unit, context: WorkflowContext) = EventURN.STORE_DEACTIVATED.urn

    override fun toEventPayload(storeId: Long, response: Unit, context: WorkflowContext) = StoreEventPayload(
        accountId = getCurrentAccountId(context),
        storeId = storeId,
    )

    override fun doExecute(storeId: Long, context: WorkflowContext) {
        marketplaceAccessApi.updateStoreStatus(
            id = storeId,
            request = UpdateStoreStatusRequest(
                status = StoreStatus.INACTIVE.name,
            ),
        )
    }
}
