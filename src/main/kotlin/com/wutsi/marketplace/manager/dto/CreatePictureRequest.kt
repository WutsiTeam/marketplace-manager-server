package com.wutsi.marketplace.manager.dto

import javax.validation.constraints.NotBlank

public data class CreatePictureRequest(
    public val productId: Long = 0,
    @get:NotBlank
    public val url: String = "",
)
