package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.DeletePictureDelegate
import kotlin.Long
import kotlin.Unit
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class DeletePictureController(
  public val `delegate`: DeletePictureDelegate,
) {
  @DeleteMapping("/v1/pictures/{id}")
  public fun invoke(@PathVariable(name="id") id: Long): Unit {
    delegate.invoke(id)
  }
}
