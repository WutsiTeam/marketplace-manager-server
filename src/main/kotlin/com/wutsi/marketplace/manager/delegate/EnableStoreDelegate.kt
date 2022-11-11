package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.dto.EnableStoreResponse
import com.wutsi.marketplace.manager.workflow.EnableStoreWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class EnableStoreDelegate(private val workflow: EnableStoreWorkflow) {
    fun invoke(): EnableStoreResponse {
        return workflow.execute(null, WorkflowContext())
    }
}
