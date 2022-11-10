package com.wutsi.marketplace.manager.workflow.product

import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.manager.dto.CreateProductRequest
import com.wutsi.marketplace.manager.dto.CreateProductResponse
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.StoreShouldNotHaveTooManyProductsRule
import org.springframework.stereotype.Service

@Service
class CreateProductWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<CreateProductRequest, CreateProductResponse>(eventStream) {
    override fun getProductId(context: WorkflowContext<CreateProductRequest, CreateProductResponse>): Long? =
        null

    override fun getAdditionalRules(account: Account, store: Store?, product: Product?) = listOf(
        store?.let { StoreShouldNotHaveTooManyProductsRule(it, regulationEngine) }
    )

    override fun doExecute(context: WorkflowContext<CreateProductRequest, CreateProductResponse>) {
        val account = getCurrentAccount(context)
        val request = context.request!!
        val response = marketplaceAccessApi.createProduct(
            request = com.wutsi.marketplace.access.dto.CreateProductRequest(
                storeId = account.storeId!!,
                pictureUrl = request.pictureUrl,
                title = request.title,
                summary = request.summary,
                price = request.price,
                categoryId = request.categoryId
            )
        )
        context.response = CreateProductResponse(
            productId = response.productId
        )
    }
}
