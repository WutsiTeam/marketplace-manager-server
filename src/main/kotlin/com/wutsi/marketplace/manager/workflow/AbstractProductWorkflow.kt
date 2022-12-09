package com.wutsi.marketplace.manager.workflow

import com.wutsi.event.ProductEventPayload
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.Rule
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.AccountShouldBeActiveRule
import com.wutsi.workflow.rule.account.AccountShouldBeBusinessRule
import com.wutsi.workflow.rule.account.AccountShouldBeOwnerOfProductRule
import com.wutsi.workflow.rule.account.AccountShouldBeOwnerOfStoreRule
import com.wutsi.workflow.rule.account.AccountShouldHaveStoreRule
import com.wutsi.workflow.rule.account.StoreShouldBeActiveRule

abstract class AbstractProductWorkflow<Req, Resp>(eventStream: EventStream) :
    AbstractMarketplaceWorkflow<Req, Resp, ProductEventPayload>(eventStream) {
    override fun getEventType(request: Req, response: Resp, context: WorkflowContext): String? = null
    override fun toEventPayload(request: Req, response: Resp, context: WorkflowContext): ProductEventPayload? = null
    protected abstract fun getProductId(request: Req, context: WorkflowContext): Long?

    override fun getValidationRules(request: Req, context: WorkflowContext): RuleSet {
        val account = getCurrentAccount(context)
        val store = account.storeId?.let {
            getCurrentStore(account)
        }
        val product = getProduct(request, context)

        val rules = mutableListOf(
            AccountShouldBeBusinessRule(account),
            AccountShouldBeActiveRule(account),
            AccountShouldHaveStoreRule(account),
            store?.let { AccountShouldBeOwnerOfStoreRule(account, it) },
            store?.let { StoreShouldBeActiveRule(it) },
            product?.let { AccountShouldBeOwnerOfProductRule(account, it) }
        )
        rules.addAll(getAdditionalRules(account, store, product))
        return RuleSet(
            rules.filterNotNull()
        )
    }

    protected open fun getAdditionalRules(account: Account, store: Store?, product: Product?): List<Rule?> = emptyList()

    private fun getProduct(request: Req, context: WorkflowContext): Product? =
        getProductId(request, context)?.let {
            marketplaceAccessApi.getProduct(it).product
        }
}
