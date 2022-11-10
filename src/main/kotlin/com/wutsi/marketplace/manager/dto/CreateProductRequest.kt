package com.wutsi.marketplace.manager.dto

import javax.validation.constraints.NotBlank
import kotlin.Long
import kotlin.String

public data class CreateProductRequest(
    @get:NotBlank
    public val pictureUrl: String = "",
    public val categoryId: Long? = null,
    public val title: String? = null,
    public val summary: String? = null,
    public val price: Long? = null
)
