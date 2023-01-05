package com.wutsi.marketplace.manager.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

public data class SearchDiscountRequest(
    public val storeId: Long = 0,
    public val productIds: List<Long> = emptyList(),
    public val discountIds: List<Long> = emptyList(),
    public val type: String? = null,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val date: LocalDate? = null,
    public val limit: Int = 100,
    public val offset: Int = 0,
)
