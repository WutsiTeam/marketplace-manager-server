package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.ActivateStoreDelegate
import com.wutsi.marketplace.manager.dto.EnableStoreResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class ActivateStoreController(
    public val `delegate`: ActivateStoreDelegate
) {
    @PostMapping("/v1/stores")
    public fun invoke(): EnableStoreResponse = delegate.invoke()
}
