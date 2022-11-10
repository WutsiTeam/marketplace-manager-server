package com.wutsi.marketplace.manager.workflow.product

import com.wutsi.marketplace.manager.dto.UpdateProductStatusRequest
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class UpdateProductStatusWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<UpdateProductStatusRequest, Void>(eventStream) {
    override fun getProductId(context: WorkflowContext<UpdateProductStatusRequest, Void>): Long? =
        context.request?.productId

    override fun doExecute(context: WorkflowContext<UpdateProductStatusRequest, Void>) {
        val request = context.request!!
        marketplaceAccessApi.updateProductStatus(
            id = request.productId,
            request = com.wutsi.marketplace.access.dto.UpdateProductStatusRequest(
                status = request.status
            )
        )
    }
}
