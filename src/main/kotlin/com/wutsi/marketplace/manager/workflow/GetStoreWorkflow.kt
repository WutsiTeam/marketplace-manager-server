package com.wutsi.marketplace.manager.workflow

import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.manager.dto.GetStoreResponse
import com.wutsi.marketplace.manager.dto.Store
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class GetStoreWorkflow(
    eventStream: EventStream
) : AbstractStoreWorkflow<Long, GetStoreResponse>(eventStream) {
    override fun getEventType(): String? = null

    override fun toEventPayload(
        productId: Long,
        response: GetStoreResponse,
        context: WorkflowContext
    ): StoreEventPayload? = null

    override fun getValidationRules(request: Long, context: WorkflowContext) = RuleSet.NONE

    override fun doExecute(storeId: Long, context: WorkflowContext): GetStoreResponse {
        val store = marketplaceAccessApi.getStore(storeId).store
        return GetStoreResponse(
            store = Store(
                id = storeId,
                accountId = store.accountId,
                currency = store.currency,
                productCount = store.productCount,
                publishedProductCount = store.publishedProductCount,
                created = store.created,
                updated = store.updated,
                deactivated = store.deactivated,
                status = store.status
            )
        )
    }

    private fun toPictureThumbnail(picture: PictureSummary) = com.wutsi.marketplace.manager.dto.PictureSummary(
        id = picture.id,
        url = picture.url
    )
}
