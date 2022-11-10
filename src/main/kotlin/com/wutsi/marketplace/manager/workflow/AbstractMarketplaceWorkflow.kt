package com.wutsi.marketplace.manager.workflow

import com.wutsi.checkout.manager.util.SecurityUtil
import com.wutsi.marketplace.access.MarketplaceAccessApi
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.membership.access.MembershipAccessApi
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.regulation.RegulationEngine
import com.wutsi.workflow.AbstractWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractMarketplaceWorkflow<Req, Resp, Ev>(eventStream: EventStream) :
    AbstractWorkflow<Req, Resp, Ev>(eventStream) {
    @Autowired
    protected lateinit var marketplaceAccessApi: MarketplaceAccessApi

    @Autowired
    protected lateinit var membershipAccessApi: MembershipAccessApi

    @Autowired
    protected lateinit var regulationEngine: RegulationEngine

    protected fun getCurrentAccountId(context: WorkflowContext<Req, Resp>): Long =
        context.accountId ?: SecurityUtil.getAccountId()

    protected fun getCurrentAccount(context: WorkflowContext<Req, Resp>): Account {
        val accountId = context.accountId ?: SecurityUtil.getAccountId()
        return membershipAccessApi.getAccount(accountId).account
    }

    protected fun getCurrentStore(account: Account): Store? =
        account.storeId?.let {
            marketplaceAccessApi.getStore(it).store
        }
}
