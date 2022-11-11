package com.wutsi.marketplace.manager.workflow

import com.wutsi.marketplace.access.dto.CreatePictureRequest
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.manager.dto.AddPictureRequest
import com.wutsi.marketplace.manager.dto.AddPictureResponse
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.ProductShouldNotHaveTooManyPicturesRule
import org.springframework.stereotype.Service

@Service
class AddPictureWorkflow(
    eventStream: EventStream
) : AbstractProductWorkflow<AddPictureRequest, AddPictureResponse>(eventStream) {
    override fun getProductId(request: AddPictureRequest, context: WorkflowContext) =
        request.productId

    override fun getAdditionalRules(account: Account, store: Store?, product: Product?) = listOf(
        product?.let { ProductShouldNotHaveTooManyPicturesRule(it, regulationEngine) }
    )

    override fun doExecute(request: AddPictureRequest, context: WorkflowContext): AddPictureResponse {
        val response = marketplaceAccessApi.createPicture(
            request = CreatePictureRequest(
                productId = request.productId,
                url = request.url
            )
        )
        return AddPictureResponse(
            pictureId = response.pictureId
        )
    }
}
