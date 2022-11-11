package com.wutsi.marketplace.manager.workflow.product

import com.wutsi.marketplace.manager.dto.UpdateProductAttributeRequest
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class UpdateProductAttributeWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<UpdateProductAttributeRequest, Unit>(eventStream) {
    override fun getProductId(request: UpdateProductAttributeRequest, context: WorkflowContext): Long? =
        request.productId

    override fun doExecute(request: UpdateProductAttributeRequest, context: WorkflowContext) {
        marketplaceAccessApi.updateProductAttribute(
            id = request.productId,
            request = com.wutsi.marketplace.access.dto.UpdateProductAttributeRequest(
                name = request.name,
                value = request.value
            )
        )
    }
}
