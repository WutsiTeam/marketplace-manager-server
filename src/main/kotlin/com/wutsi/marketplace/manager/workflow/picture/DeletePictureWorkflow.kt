package com.wutsi.marketplace.manager.workflow.picture

import com.wutsi.marketplace.manager.workflow.product.AbstractProductWorkflow
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class DeletePictureWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<Long, Void>(eventStream) {
    override fun getProductId(context: WorkflowContext<Long, Void>): Long? =
        null

    override fun doExecute(context: WorkflowContext<Long, Void>) {
        val pictureId = context.request!!
        marketplaceAccessApi.deletePicture(pictureId)
    }
}
