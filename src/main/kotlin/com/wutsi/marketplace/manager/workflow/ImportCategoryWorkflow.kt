package com.wutsi.marketplace.manager.workflow

import com.wutsi.marketplace.access.dto.SaveCategoryRequest
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.stream.EventStream
import com.wutsi.workflow.RuleSet
import com.wutsi.workflow.WorkflowContext
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import java.net.URL
import java.util.Locale
import java.util.Scanner

@Service
class ImportCategoryWorkflow(
    eventStream: EventStream,
    private val logger: KVLogger,
) : AbstractMarketplaceWorkflow<String, Unit, Void>(eventStream) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImportCategoryWorkflow::class.java)
    }

    override fun getEventType(language: String, response: Unit, context: WorkflowContext): String? = null
    override fun toEventPayload(language: String, response: Unit, context: WorkflowContext): Void? = null
    override fun getValidationRules(language: String, context: WorkflowContext) = RuleSet.NONE

    override fun doExecute(language: String, context: WorkflowContext) {
        // Language
        if (language != null) {
            LocaleContextHolder.setLocale(Locale(language))
        }

        // URL
        val url = getUrl(language)
        logger.add("url", url)

        // Load
        var rows = 0
        var errors = 0
        val scanner = Scanner(url.readText(charset = Charsets.UTF_8))
        val ids = mutableMapOf<String, Long>()
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            try {
                process(line, ids)
            } catch (ex: Exception) {
                LOGGER.warn("$rows - $line", ex)
                errors++
            } finally {
                rows++
            }
        }

        logger.add("rows", rows)
        logger.add("errors", errors)
    }

    private fun process(line: String, ids: MutableMap<String, Long>) {
        if (line.startsWith("#")) { // Comment
            return
        }

        val id = extractId(line)
        val parent = extractParent(line)
        val title = extractTitle(line)
        try {
            marketplaceAccessApi.saveCategory(
                id = id,
                request = SaveCategoryRequest(
                    parentId = parent?.let { ids[parent]!! },
                    title = title,
                ),
            )
        } finally {
            val longTitle = extractLongTitle(line)
            ids[longTitle] = id
        }
    }

    private fun extractId(line: String): Long {
        val i = line.indexOf("-")
        return line.substring(0, i - 1).toLong()
    }

    private fun extractParent(line: String): String? {
        val i = line.indexOf("-")
        val j = line.lastIndexOf(">")
        return if (j < 0) {
            null
        } else {
            line.substring(i + 1, j - 1).trim()
        }
    }

    private fun extractTitle(line: String): String {
        val j = line.lastIndexOf(">")
        return if (j < 0) {
            val i = line.indexOf("-")
            line.substring(i + 1).trim()
        } else {
            line.substring(j + 1).trim()
        }
    }

    private fun extractLongTitle(line: String): String {
        val i = line.indexOf("-")
        return line.substring(i + 1).trim()
    }

    private fun getUrl(language: String?): URL = when (language?.lowercase()) {
        "fr" -> URL("https://www.google.com/basepages/producttype/taxonomy-with-ids.fr-FR.txt")
        else -> URL("https://www.google.com/basepages/producttype/taxonomy-with-ids.en-US.txt")
    }
}
