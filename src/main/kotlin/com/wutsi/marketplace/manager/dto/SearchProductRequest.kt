package com.wutsi.marketplace.manager.dto

public data class SearchProductRequest(
    public val productIds: List<Long> = emptyList(),
    public val categoryIds: List<Long> = emptyList(),
    public val storeId: Long? = null,
    public val limit: Int = 100,
    public val offset: Int = 0,
    public val status: String = ""
)
