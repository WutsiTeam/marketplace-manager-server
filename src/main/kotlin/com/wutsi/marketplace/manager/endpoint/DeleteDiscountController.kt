package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.DeleteDiscountDelegate
import kotlin.Long
import kotlin.Unit
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class DeleteDiscountController(
  public val `delegate`: DeleteDiscountDelegate,
) {
  @DeleteMapping("/v1/discounts/{id}")
  public fun invoke(@PathVariable(name="id") id: Long): Unit {
    delegate.invoke(id)
  }
}
