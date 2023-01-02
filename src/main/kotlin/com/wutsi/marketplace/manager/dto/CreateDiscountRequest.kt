package com.wutsi.marketplace.manager.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.Boolean
import kotlin.Int
import kotlin.String

public data class CreateDiscountRequest(
    @get:NotBlank
    @get:Size(max = 30)
    public val name: String = "",
    @get:Min(1)
    public val rate: Int = 0,
    @get:NotNull
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val starts: LocalDate = LocalDate.now(),
    @get:NotNull
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val ends: LocalDate = LocalDate.now(),
    public val allProducts: Boolean = false,
)
