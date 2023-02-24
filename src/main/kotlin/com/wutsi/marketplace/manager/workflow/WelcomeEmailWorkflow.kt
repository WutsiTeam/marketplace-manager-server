package com.wutsi.marketplace.manager.workflow

import com.wutsi.mail.MailContext
import com.wutsi.mail.MailFilterSet
import com.wutsi.mail.Merchant
import com.wutsi.membership.access.MembershipAccessApi
import com.wutsi.membership.access.dto.Account
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.messaging.Message
import com.wutsi.platform.core.messaging.MessagingServiceProvider
import com.wutsi.platform.core.messaging.MessagingType
import com.wutsi.platform.core.messaging.Party
import com.wutsi.workflow.Workflow
import com.wutsi.workflow.WorkflowContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.Locale

@Service
class WelcomeEmailWorkflow(
    private val membershipAccessApi: MembershipAccessApi,
    private val templateEngine: TemplateEngine,
    private val mailFilterSet: MailFilterSet,
    private val cache: Cache,

    @Value("\${wutsi.application.asset-url}") private val assetUrl: String,
    @Value("\${wutsi.application.webapp-url}") private val webappUrl: String,
    @Value("\${wutsi.application.website-url}") private val websiteUrl: String,
    @Value("\${wutsi.application.email.welcome.debug}") private val debug: Boolean,
) : Workflow<Long, Unit> {

    @Autowired
    private lateinit var messagingServiceProvider: MessagingServiceProvider

    @Autowired
    protected lateinit var logger: KVLogger

    @Autowired
    private lateinit var messages: MessageSource

    override fun execute(accountId: Long, context: WorkflowContext) {
        if (isEmailSent(accountId)) {
            logger.add("email_already_sent", true)
            return
        }

        val merchant = membershipAccessApi.getAccount(accountId).account
        logger.add("merchant_email", merchant.email)
        createMessage(merchant)?.let {
            val messageId = sendEmail(message = debug(it))
            logger.add("message_id_email", messageId)

            emailSent(accountId)
        }
    }

    private fun createMessage(
        merchant: Account,
    ): Message? =
        merchant.email?.let {
            val locale = Locale(merchant.language)

            Message(
                recipient = Party(
                    email = it,
                    displayName = merchant.displayName,
                ),
                subject = getText("email.welcome.subject", locale = locale),
                body = generateBody(merchant, locale),
                mimeType = "text/html;charset=UTF-8",
            )
        }

    private fun generateBody(merchant: Account, locale: Locale): String {
        val context = createMailContext(merchant)
        val body = templateEngine.process(
            "welcome.html",
            Context(
                locale,
                mapOf(
                    "merchant" to context.merchant,
                    "websiteUrl" to websiteUrl,
                ),
            ),
        )
        return mailFilterSet.filter(
            body = body,
            context = context,
        )
    }

    private fun debug(message: Message): Message {
        if (debug) {
            val logger = getLogger()
            logger.info("Recipient Address: ${message.recipient.displayName}< ${message.recipient.email}>")
            message.recipient.deviceToken?.let {
                logger.info("Recipient Device: $it")
            }
            message.subject?.let {
                logger.info("Subject: $it")
            }
            logger.info("\n${message.body}\n")
        }
        return message
    }

    private fun getLogger(): Logger =
        LoggerFactory.getLogger(this::class.java)

    private fun createMailContext(merchant: Account) = MailContext(
        template = "wutsi",
        assetUrl = assetUrl,
        merchant = Merchant(
            url = if (merchant.name == null) "$webappUrl/u/${merchant.id}" else "$webappUrl/@${merchant.name}",
            name = merchant.displayName,
            logoUrl = merchant.pictureUrl,
            phoneNumber = merchant.phone.number,
            websiteUrl = merchant.website,
            twitterId = merchant.twitterId,
            facebookId = merchant.facebookId,
            youtubeId = merchant.youtubeId,
            instagramId = merchant.instagramId,
            category = null,
            location = null,
            whatsapp = merchant.whatsapp,
            country = merchant.country,
        ),
    )

    private fun sendEmail(message: Message): String? {
        if (message.recipient.email.isNullOrEmpty()) {
            return null
        }
        val sender = messagingServiceProvider.get(MessagingType.EMAIL)
        return sender.send(message)
    }

    private fun emailSent(accountId: Long) {
        cache.put(getEmailCacheKey(accountId), "1")
    }

    private fun isEmailSent(accountId: Long): Boolean =
        cache.get(getEmailCacheKey(accountId), String::class.java) != null

    private fun getEmailCacheKey(accountId: Long): String =
        "welcome_email_$accountId"

    private fun getText(key: String, args: Array<Any> = emptyArray(), locale: Locale): String =
        messages.getMessage(key, args, locale)
}
