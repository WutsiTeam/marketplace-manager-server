package com.wutsi.marketplace.manager.workflow.store

import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.manager.event.StoreEventPayload
import com.wutsi.marketplace.manager.workflow.AbstractMarketplaceWorkflow
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.Rule
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.AccountShouldBeActiveRule
import com.wutsi.workflow.rule.account.AccountShouldBeBusinessRule

abstract class AbstractStoreWorkflow<Req, Resp>(eventStream: EventStream) :
    AbstractMarketplaceWorkflow<Req, Resp, StoreEventPayload>(eventStream) {
    override fun getValidationRules(context: WorkflowContext<Req, Resp>): RuleSet {
        val account = getCurrentAccount(context)
        val store = account.storeId?.let {
            getCurrentStore(account)
        }
        val rules = mutableListOf<Rule?>(
            AccountShouldBeActiveRule(account),
            AccountShouldBeBusinessRule(account)
        )
        rules.addAll(getAdditionalRules(account, store))
        return RuleSet(
            rules.filterNotNull()
        )
    }

    protected open fun getAdditionalRules(account: Account, store: Store?): List<Rule?> = emptyList()
}
