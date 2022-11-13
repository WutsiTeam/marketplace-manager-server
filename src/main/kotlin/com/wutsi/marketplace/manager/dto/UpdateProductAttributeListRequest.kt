package com.wutsi.marketplace.manager.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

public data class UpdateProductAttributeListRequest(
    val productId: Long = -1,
    @get:NotNull
    @get:NotEmpty
    public val attributes: List<ProductAttribute> = emptyList()
)
