package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.dto.UpdateProductAttributeRequest
import com.wutsi.marketplace.manager.workflow.product.UpdateProductAttributeWorkflow
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
public class UpdateProductAttributeDelegate(
    private val workflow: UpdateProductAttributeWorkflow,
    private val logger: KVLogger
) {
    public fun invoke(request: UpdateProductAttributeRequest) {
        logger.add("request_product_id", request.productId)
        logger.add("request_name", request.name)
        logger.add("request_value", request.value)

        workflow.execute(request, WorkflowContext())
    }
}
