package com.wutsi.marketplace.manager.workflow

import com.wutsi.marketplace.manager.dto.UpdateProductStatusRequest
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class UpdateProductStatusWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<UpdateProductStatusRequest, Unit>(eventStream) {
    override fun getProductId(request: UpdateProductStatusRequest, context: WorkflowContext): Long? =
        request.productId

    override fun doExecute(request: UpdateProductStatusRequest, context: WorkflowContext) {
        marketplaceAccessApi.updateProductStatus(
            id = request.productId,
            request = com.wutsi.marketplace.access.dto.UpdateProductStatusRequest(
                status = request.status
            )
        )
    }
}
