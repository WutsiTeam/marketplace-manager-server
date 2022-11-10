package com.wutsi.marketplace.manager.workflow.store

import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.dto.UpdateStoreStatusRequest
import com.wutsi.marketplace.access.enums.StoreStatus
import com.wutsi.marketplace.manager.event.EventURN
import com.wutsi.marketplace.manager.event.StoreEventPayload
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.AccountShouldBeOwnerOfStoreRule
import com.wutsi.workflow.rule.account.AccountShouldHaveStoreRule
import org.springframework.stereotype.Service

@Service
class SuspendStoreWorkflow(
    eventStream: EventStream
) : AbstractStoreWorkflow<Void, Long>(eventStream) {
    override fun getEventType() = EventURN.STORE_SUSPENDED.urn

    override fun toEventPayload(context: WorkflowContext<Void, Long>) = context.response?.let {
        StoreEventPayload(
            accountId = getCurrentAccountId(context),
            storeId = it
        )
    }

    override fun getAdditionalRules(account: Account, store: Store?) = listOf(
        AccountShouldHaveStoreRule(account),
        store?.let { AccountShouldBeOwnerOfStoreRule(account, it) }
    )

    override fun doExecute(context: WorkflowContext<Void, Long>) {
        val account = getCurrentAccount(context)
        marketplaceAccessApi.updateStoreStatus(
            id = account.storeId!!,
            request = UpdateStoreStatusRequest(
                status = StoreStatus.SUSPENDED.name
            )
        )
        context.response = account.storeId
    }
}
