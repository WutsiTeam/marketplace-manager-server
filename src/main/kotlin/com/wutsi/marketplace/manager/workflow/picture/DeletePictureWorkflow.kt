package com.wutsi.marketplace.manager.workflow.picture

import com.wutsi.marketplace.manager.workflow.product.AbstractProductWorkflow
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class DeletePictureWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<Long, Unit>(eventStream) {
    override fun getProductId(pictureId: Long, context: WorkflowContext): Long? =
        null

    override fun doExecute(pictureId: Long, context: WorkflowContext) {
        marketplaceAccessApi.deletePicture(pictureId)
    }
}
