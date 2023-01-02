package com.wutsi.marketplace.manager.workflow

import com.wutsi.marketplace.access.dto.Discount
import com.wutsi.marketplace.manager.dto.UpdateDiscountRequest
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.Rule
import com.wutsi.workflow.WorkflowContext
import com.wutsi.workflow.rule.account.DiscountShouldHaveDurationLessThan30DaysRule
import com.wutsi.workflow.rule.account.DiscountShouldHaveStartDateBeforeEndDateRule
import org.springframework.stereotype.Service

@Service
class UpdateDiscountWorkflow(
    eventStream: EventStream,
) : AbstractDiscountWorkflow<UpdateDiscountRequest, Unit>(eventStream) {
    companion object {
        const val ATTRIBUTE_ID = "id"
    }

    override fun getAdditionalRules(request: UpdateDiscountRequest): List<Rule?> {
        val discount = Discount(
            starts = request.starts,
            ends = request.ends,
        )
        return listOf(
            DiscountShouldHaveDurationLessThan30DaysRule(discount),
            DiscountShouldHaveStartDateBeforeEndDateRule(discount),
        )
    }

    override fun doExecute(
        request: UpdateDiscountRequest,
        context: WorkflowContext,
    ) {
        marketplaceAccessApi.updateDiscount(
            id = context.data[ATTRIBUTE_ID]!! as Long,
            request = com.wutsi.marketplace.access.dto.UpdateDiscountRequest(
                name = request.name,
                starts = request.starts,
                ends = request.ends,
                allProducts = request.allProducts,
                rate = request.rate,
            ),
        )
    }
}
