package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.DeactivateStoreDelegate
import kotlin.Unit
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class DeactivateStoreController(
  public val `delegate`: DeactivateStoreDelegate,
) {
  @DeleteMapping("/v1/stores")
  public fun invoke(): Unit {
    delegate.invoke()
  }
}
