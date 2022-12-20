package com.wutsi.marketplace.manager.delegate

import com.wutsi.marketplace.manager.workflow.ImportCategoryWorkflow
import com.wutsi.workflow.WorkflowContext
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
public class ImportCategoryDelegate(private val workflow: ImportCategoryWorkflow) {
    @Async
    public fun invoke(language: String) {
        workflow.execute(language, WorkflowContext())
    }
}
