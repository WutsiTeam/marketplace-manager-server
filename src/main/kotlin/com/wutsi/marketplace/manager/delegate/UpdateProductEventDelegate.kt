package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.dto.UpdateProductEventRequest
import com.wutsi.marketplace.manager.workflow.UpdateProductEventWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
public class UpdateProductEventDelegate(private val workflow: UpdateProductEventWorkflow) {
    public fun invoke(request: UpdateProductEventRequest) {
        workflow.execute(request, WorkflowContext())
    }
}
