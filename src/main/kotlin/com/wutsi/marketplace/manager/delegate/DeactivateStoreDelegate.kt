package com.wutsi.marketplace.manager.delegate

import com.wutsi.checkout.manager.util.SecurityUtil
import com.wutsi.marketplace.manager.workflow.DeactivateStoreWorkflow
import com.wutsi.membership.access.MembershipAccessApi
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class DeactivateStoreDelegate(
    private val workflow: DeactivateStoreWorkflow,
    private val membershipAccessApi: MembershipAccessApi,
    private val logger: KVLogger,
) {
    fun invoke() {
        val accountId = SecurityUtil.getAccountId()
        logger.add("account_id", accountId)

        val account = membershipAccessApi.getAccount(accountId).account
        account.storeId?.let {
            logger.add("store_id", it)
            workflow.execute(it, WorkflowContext())
        }
    }
}
