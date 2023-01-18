package com.wutsi.marketplace.manager.workflow

import com.wutsi.event.StoreEventPayload
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.Rule
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.AccountShouldBeActiveRule

abstract class AbstractStoreWorkflow<Req, Resp>(eventStream: EventStream) :
    AbstractMarketplaceWorkflow<Req, Resp, StoreEventPayload>(eventStream) {
    override fun getValidationRules(request: Req, context: WorkflowContext): RuleSet {
        val account = getCurrentAccount(context)
        val rules = mutableListOf<Rule?>(
            AccountShouldBeActiveRule(account),
        )
        rules.addAll(getAdditionalRules(account, context))
        return RuleSet(
            rules.filterNotNull(),
        )
    }

    protected open fun getAdditionalRules(account: Account, ctx: WorkflowContext): List<Rule?> = emptyList()
}
