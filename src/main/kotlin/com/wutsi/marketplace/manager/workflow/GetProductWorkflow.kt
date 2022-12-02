package com.wutsi.marketplace.manager.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.event.ProductEventPayload
import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.manager.dto.GetProductResponse
import com.wutsi.marketplace.manager.dto.Product
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class GetProductWorkflow(
    eventStream: EventStream,
    private val objectMapper: ObjectMapper
) : AbstractProductWorkflow<Long, GetProductResponse>(eventStream) {
    override fun getEventType(): String? = null

    override fun toEventPayload(
        productId: Long,
        response: GetProductResponse,
        context: WorkflowContext
    ): ProductEventPayload? = null

    override fun getProductId(productId: Long, context: WorkflowContext): Long? = null

    override fun getValidationRules(request: Long, context: WorkflowContext) = RuleSet.NONE

    override fun doExecute(productId: Long, context: WorkflowContext): GetProductResponse {
        val product = marketplaceAccessApi.getProduct(productId).product
        return GetProductResponse(
            product = objectMapper.readValue(
                objectMapper.writeValueAsString(product),
                Product::class.java
            )
        )
    }

    private fun toPictureThumbnail(picture: PictureSummary) = com.wutsi.marketplace.manager.dto.PictureSummary(
        id = picture.id,
        url = picture.url
    )
}
