package com.wutsi.marketplace.manager.delegate

import com.wutsi.marketplace.manager.dto.UpdateDiscountRequest
import com.wutsi.marketplace.manager.workflow.UpdateDiscountWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class UpdateDiscountDelegate(private val workflow: UpdateDiscountWorkflow) {
    fun invoke(id: Long, request: UpdateDiscountRequest) {
        workflow.execute(
            request,
            WorkflowContext(
                data = mutableMapOf(
                    UpdateDiscountWorkflow.ATTRIBUTE_ID to id,
                ),
            ),
        )
    }
}
