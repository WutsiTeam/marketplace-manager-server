package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.workflow.store.SuspendStoreWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
public class SuspendStoreDelegate(private val workflow: SuspendStoreWorkflow) {
    public fun invoke() {
        workflow.execute(WorkflowContext())
    }
}
