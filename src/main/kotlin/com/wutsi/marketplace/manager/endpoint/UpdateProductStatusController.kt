package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.UpdateProductStatusDelegate
import com.wutsi.marketplace.manager.dto.UpdateProductStatusRequest
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class UpdateProductStatusController(
    public val `delegate`: UpdateProductStatusDelegate
) {
    @PostMapping("/v1/products/status")
    public fun invoke(
        @Valid @RequestBody
        request: UpdateProductStatusRequest
    ) {
        delegate.invoke(request)
    }
}
