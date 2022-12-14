package com.wutsi.marketplace.manager.dto

import javax.validation.constraints.NotBlank

public data class CreateProductRequest(
    public val pictureUrl: String? = null,
    public val categoryId: Long? = null,
    @get:NotBlank
    public val title: String = "",
    public val summary: String? = null,
    public val price: Long? = null,
    public val quantity: Int? = null,
    public val type: String = ""
)
