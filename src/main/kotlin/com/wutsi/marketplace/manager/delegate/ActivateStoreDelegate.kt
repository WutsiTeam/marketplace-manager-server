package com.wutsi.marketplace.manager.delegate

import com.wutsi.marketplace.manager.dto.ActivateStoreResponse
import com.wutsi.marketplace.manager.workflow.ActivateStoreWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class ActivateStoreDelegate(private val workflow: ActivateStoreWorkflow) {
    fun invoke(): ActivateStoreResponse {
        return workflow.execute(null, WorkflowContext())
    }
}
