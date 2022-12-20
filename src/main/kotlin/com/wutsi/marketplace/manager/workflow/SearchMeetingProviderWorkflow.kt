package com.wutsi.marketplace.manager.workflow

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.event.ProductEventPayload
import com.wutsi.marketplace.manager.dto.MeetingProviderSummary
import com.wutsi.marketplace.manager.dto.SearchMeetingProviderResponse
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import org.springframework.stereotype.Service

@Service
class SearchMeetingProviderWorkflow(
    private val objectMapper: ObjectMapper,
    eventStream: EventStream,
) : AbstractProductWorkflow<Void?, SearchMeetingProviderResponse>(eventStream) {
    override fun getEventType(
        request: Void?,
        response: SearchMeetingProviderResponse,
        context: WorkflowContext,
    ): String? = null

    override fun toEventPayload(
        request: Void?,
        response: SearchMeetingProviderResponse,
        context: WorkflowContext,
    ): ProductEventPayload? = null

    override fun getValidationRules(request: Void?, context: WorkflowContext) = RuleSet.NONE

    override fun getProductId(request: Void?, context: WorkflowContext): Long? = null

    override fun doExecute(request: Void?, context: WorkflowContext): SearchMeetingProviderResponse {
        val response = marketplaceAccessApi.searchMeetingProvider()
        return SearchMeetingProviderResponse(
            meetingProviders = response.meetingProviders.map {
                objectMapper.readValue(
                    objectMapper.writeValueAsString(it),
                    MeetingProviderSummary::class.java,
                )
            },
        )
    }
}
