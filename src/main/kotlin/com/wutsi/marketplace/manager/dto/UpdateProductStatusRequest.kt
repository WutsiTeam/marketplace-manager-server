package com.wutsi.marketplace.manager.dto

import javax.validation.constraints.NotBlank
import kotlin.Long
import kotlin.String

public data class UpdateProductStatusRequest(
    public val productId: Long = 0,
    @get:NotBlank
    public val status: String = ""
)
