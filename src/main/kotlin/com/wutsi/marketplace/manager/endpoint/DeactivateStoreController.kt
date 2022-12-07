package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.SuspendStoreDelegate
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class DeactivateStoreController(
    public val `delegate`: SuspendStoreDelegate
) {
    @DeleteMapping("/v1/stores")
    public fun invoke() {
        delegate.invoke()
    }
}
