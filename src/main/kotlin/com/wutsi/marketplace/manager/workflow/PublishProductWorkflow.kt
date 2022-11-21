package com.wutsi.marketplace.manager.workflow

import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.dto.UpdateProductStatusRequest
import com.wutsi.marketplace.access.enums.ProductStatus
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.Rule
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.ProductShouldHavePictureRule
import com.wutsi.workflow.rule.account.ProductShouldHaveStockRule
import org.springframework.stereotype.Service

@Service
class PublishProductWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<Long, Unit>(eventStream) {
    override fun getAdditionalRules(account: Account, store: Store?, product: Product?): List<Rule?> =
        listOf(
            product?.let { ProductShouldHavePictureRule(product) },
            product?.let { ProductShouldHaveStockRule(product) }
        )

    override fun getProductId(productId: Long, context: WorkflowContext): Long? =
        productId

    override fun doExecute(productId: Long, context: WorkflowContext) {
        marketplaceAccessApi.updateProductStatus(
            id = productId,
            request = UpdateProductStatusRequest(
                status = ProductStatus.PUBLISHED.name
            )
        )
    }
}
