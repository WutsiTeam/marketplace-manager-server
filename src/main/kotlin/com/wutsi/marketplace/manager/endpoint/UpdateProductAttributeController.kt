package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.UpdateProductAttributeDelegate
import com.wutsi.marketplace.manager.dto.UpdateProductAttributeListRequest
import javax.validation.Valid
import kotlin.Unit
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class UpdateProductAttributeController(
  public val `delegate`: UpdateProductAttributeDelegate,
) {
  @PostMapping("/v1/products/attributes")
  public fun invoke(@Valid @RequestBody request: UpdateProductAttributeListRequest): Unit {
    delegate.invoke(request)
  }
}
