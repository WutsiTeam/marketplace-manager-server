package com.wutsi.marketplace.manager.workflow.store

import com.wutsi.marketplace.access.dto.CreateStoreRequest
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.manager.dto.EnableStoreResponse
import com.wutsi.marketplace.manager.event.EventURN
import com.wutsi.marketplace.manager.event.StoreEventPayload
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.CountryShouldSupportStoreRule
import org.springframework.stereotype.Service

@Service
class EnableStoreWorkflow(
    eventStream: EventStream
) : AbstractStoreWorkflow<Void, EnableStoreResponse>(eventStream) {
    override fun getEventType() = EventURN.STORE_ENABLED.urn

    override fun toEventPayload(context: WorkflowContext<Void, EnableStoreResponse>) = StoreEventPayload(
        accountId = getCurrentAccountId(context),
        storeId = context.response!!.storeId
    )

    override fun getAdditionalRules(account: Account, store: Store?) = listOf(
        CountryShouldSupportStoreRule(account, regulationEngine)
    )

    override fun doExecute(context: WorkflowContext<Void, EnableStoreResponse>) {
        val account = getCurrentAccount(context)

        val response = marketplaceAccessApi.createStore(
            request = CreateStoreRequest(
                accountId = account.id,
                currency = regulationEngine.country(account.country).currency
            )
        )
        context.response = EnableStoreResponse(
            storeId = response.storeId
        )
    }
}
