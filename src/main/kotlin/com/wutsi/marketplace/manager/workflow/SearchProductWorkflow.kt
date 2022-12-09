package com.wutsi.marketplace.manager.workflow

import com.wutsi.event.ProductEventPayload
import com.wutsi.marketplace.manager.dto.ProductSummary
import com.wutsi.marketplace.manager.dto.SearchProductRequest
import com.wutsi.marketplace.manager.dto.SearchProductResponse
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class SearchProductWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<SearchProductRequest, SearchProductResponse>(eventStream) {
    override fun getEventType(
        request: SearchProductRequest,
        response: SearchProductResponse,
        context: WorkflowContext
    ): String? = null

    override fun toEventPayload(
        request: SearchProductRequest,
        response: SearchProductResponse,
        context: WorkflowContext
    ): ProductEventPayload? = null

    override fun getValidationRules(request: SearchProductRequest, context: WorkflowContext) = RuleSet.NONE

    override fun getProductId(request: SearchProductRequest, context: WorkflowContext): Long? = null

    override fun doExecute(request: SearchProductRequest, context: WorkflowContext): SearchProductResponse {
        val response = marketplaceAccessApi.searchProduct(
            request = com.wutsi.marketplace.access.dto.SearchProductRequest(
                limit = request.limit,
                offset = request.offset,
                categoryIds = request.categoryIds,
                productIds = request.productIds,
                storeId = request.storeId,
                sortBy = request.sortBy,
                status = request.status
            )
        )
        return SearchProductResponse(
            products = response.products.map {
                ProductSummary(
                    id = it.id,
                    title = it.title,
                    summary = it.summary,
                    price = it.price,
                    comparablePrice = it.comparablePrice,
                    currency = it.currency,
                    quantity = it.quantity,
                    status = it.status,
                    storeId = it.storeId,
                    categoryId = it.categoryId,
                    thumbnailUrl = it.thumbnailUrl
                )
            }
        )
    }
}
