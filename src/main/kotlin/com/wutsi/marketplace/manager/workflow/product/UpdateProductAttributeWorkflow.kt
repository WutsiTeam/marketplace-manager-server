package com.wutsi.marketplace.manager.workflow.product

import com.wutsi.marketplace.manager.dto.UpdateProductAttributeRequest
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class UpdateProductAttributeWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<UpdateProductAttributeRequest, Void>(eventStream) {
    override fun getProductId(context: WorkflowContext<UpdateProductAttributeRequest, Void>): Long? =
        context.request?.productId

    override fun doExecute(context: WorkflowContext<UpdateProductAttributeRequest, Void>) {
        val request = context.request!!
        marketplaceAccessApi.updateProductAttribute(
            id = request.productId,
            request = com.wutsi.marketplace.access.dto.UpdateProductAttributeRequest(
                name = request.name,
                value = request.value
            )
        )
    }
}
