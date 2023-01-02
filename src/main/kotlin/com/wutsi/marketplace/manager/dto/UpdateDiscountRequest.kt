package com.wutsi.marketplace.manager.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public data class UpdateDiscountRequest(
    @get:NotBlank
    public val name: String = "",
    public val rate: Int = 0,
    @get:NotNull
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val starts: LocalDate = LocalDate.now(),
    @get:NotNull
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val ends: LocalDate = LocalDate.now(),
    public val allProducts: Boolean = false,
)
