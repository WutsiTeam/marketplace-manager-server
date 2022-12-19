package com.wutsi.marketplace.manager.dto

import javax.validation.constraints.NotBlank

public data class CreateFileRequest(
    public val productId: Long = 0,
    @get:NotBlank
    public val url: String = "",
    @get:NotBlank
    public val contentType: String = "",
    public val contentSize: Int = 0
)
