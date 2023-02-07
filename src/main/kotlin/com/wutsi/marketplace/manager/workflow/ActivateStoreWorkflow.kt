package com.wutsi.marketplace.manager.workflow

import com.wutsi.event.EventURN
import com.wutsi.event.StoreEventPayload
import com.wutsi.marketplace.access.dto.CreateStoreRequest
import com.wutsi.marketplace.manager.dto.ActivateStoreResponse
import com.wutsi.marketplace.manager.event.InternalEventURN
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.AccountShouldBeActiveRule
import com.wutsi.workflow.rule.account.AccountShouldBeBusinessRule
import com.wutsi.workflow.rule.account.CountryShouldSupportStoreRule
import org.springframework.stereotype.Service

@Service
class ActivateStoreWorkflow(
    eventStream: EventStream,
) : AbstractStoreWorkflow<Void?, ActivateStoreResponse>(eventStream) {
    override fun getEventType(request: Void?, response: ActivateStoreResponse, context: WorkflowContext) =
        EventURN.STORE_ACTIVATED.urn

    override fun toEventPayload(request: Void?, response: ActivateStoreResponse, context: WorkflowContext) =
        StoreEventPayload(
            accountId = getCurrentAccountId(context),
            storeId = response.storeId,
        )

    override fun getAdditionalRules(account: Account, context: WorkflowContext) = listOf(
        AccountShouldBeActiveRule(account),
        AccountShouldBeBusinessRule(account),
        CountryShouldSupportStoreRule(account, regulationEngine),
    )

    override fun doExecute(request: Void?, context: WorkflowContext): ActivateStoreResponse {
        val account = getCurrentAccount(context)
        val response = marketplaceAccessApi.createStore(
            request = CreateStoreRequest(
                accountId = account.id,
                businessId = account.businessId!!,
                currency = regulationEngine.country(account.country).currency,
            ),
        )

        eventStream.enqueue(
            InternalEventURN.WELCOME_TO_MERCHANT_SUBMITTED.urn,
            StoreEventPayload(accountId = account.id, storeId = response.storeId)
        )
        return ActivateStoreResponse(
            storeId = response.storeId,
        )
    }
}
