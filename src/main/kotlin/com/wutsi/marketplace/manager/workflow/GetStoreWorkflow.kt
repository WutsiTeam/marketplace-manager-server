package com.wutsi.marketplace.manager.workflow

import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.manager.dto.GetStoreResponse
import com.wutsi.marketplace.manager.dto.Store
import com.wutsi.workflow.Workflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class GetStoreWorkflow(private val marketplaceAccessApi: MarketplaceAccessApi) : Workflow<Long, GetStoreResponse> {
    override fun execute(storeId: Long, context: WorkflowContext): GetStoreResponse {
        val store = marketplaceAccessApi.getStore(storeId).store
        return GetStoreResponse(
            store = Store(
                id = storeId,
                accountId = store.accountId,
                businessId = store.businessId,
                currency = store.currency,
                productCount = store.productCount,
                publishedProductCount = store.publishedProductCount,
                created = store.created,
                updated = store.updated,
                deactivated = store.deactivated,
                status = store.status,
            ),
        )
    }
}
