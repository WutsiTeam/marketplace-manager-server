package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.AddDiscountProductDelegate
import kotlin.Long
import kotlin.Unit
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class AddDiscountProductController(
  public val `delegate`: AddDiscountProductDelegate,
) {
  @PostMapping("/v1/discounts/{discount-id}/products/{product-id}")
  public fun invoke(@PathVariable(name="discount-id") discountId: Long,
      @PathVariable(name="product-id") productId: Long): Unit {
    delegate.invoke(discountId, productId)
  }
}
