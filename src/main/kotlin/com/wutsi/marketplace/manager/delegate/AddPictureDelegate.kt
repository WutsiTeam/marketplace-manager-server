package com.wutsi.marketplace.manager.`delegate`

import com.wutsi.marketplace.manager.dto.AddPictureRequest
import com.wutsi.marketplace.manager.dto.AddPictureResponse
import com.wutsi.marketplace.manager.workflow.AddPictureWorkflow
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class AddPictureDelegate(
    private val workflow: AddPictureWorkflow,
    private val logger: KVLogger
) {
    fun invoke(request: AddPictureRequest): AddPictureResponse {
        logger.add("request_prodcut_id", request.productId)
        logger.add("request_url", request.url)

        return workflow.execute(request, WorkflowContext())
    }
}
