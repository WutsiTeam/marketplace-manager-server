package com.wutsi.marketplace.manager.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class ProductSummary(
    public val id: Long = 0,
    public val storeId: Long = 0,
    public val thumbnailUrl: String? = null,
    public val title: String = "",
    public val summary: String? = null,
    public val price: Long? = null,
    public val comparablePrice: Long? = null,
    public val categoryId: Long? = null,
    public val currency: String = "",
    public val quantity: Int? = null,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val updated: OffsetDateTime = OffsetDateTime.now(),
    public val status: String = ""
)
