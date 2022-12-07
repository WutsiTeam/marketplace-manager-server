package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.workflow.DeactivateStoreWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class SuspendStoreDelegate(private val workflow: DeactivateStoreWorkflow) {
    fun invoke() {
        workflow.execute(null, WorkflowContext())
    }
}
