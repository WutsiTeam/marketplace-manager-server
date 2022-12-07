package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.dto.EnableStoreResponse
import com.wutsi.marketplace.manager.workflow.ActivateStoreWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class ActivateStoreDelegate(private val workflow: ActivateStoreWorkflow) {
    fun invoke(): EnableStoreResponse {
        return workflow.execute(null, WorkflowContext())
    }
}
