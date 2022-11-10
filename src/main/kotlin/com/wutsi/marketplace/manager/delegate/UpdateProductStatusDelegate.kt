package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.dto.UpdateProductStatusRequest
import com.wutsi.marketplace.manager.workflow.product.UpdateProductStatusWorkflow
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class UpdateProductStatusDelegate(
    private val workflow: UpdateProductStatusWorkflow,
    private val logger: KVLogger
) {
    fun invoke(request: UpdateProductStatusRequest) {
        logger.add("request_product_id", request.productId)
        logger.add("request_status", request.status)

        workflow.execute(WorkflowContext(request))
    }
}
