package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.ImportProductDelegate
import com.wutsi.marketplace.manager.dto.ImportProductRequest
import javax.validation.Valid
import kotlin.Unit
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class ImportProductController(
  public val `delegate`: ImportProductDelegate,
) {
  @PostMapping("/v1/products/import")
  public fun invoke(@Valid @RequestBody request: ImportProductRequest): Unit {
    delegate.invoke(request)
  }
}
