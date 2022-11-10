package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.UpdateProductAttributeDelegate
import com.wutsi.marketplace.manager.dto.UpdateProductAttributeRequest
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class UpdateProductAttributeController(
    public val `delegate`: UpdateProductAttributeDelegate
) {
    @PostMapping("/v1/products/attributes")
    public fun invoke(
        @Valid @RequestBody
        request: UpdateProductAttributeRequest
    ) {
        delegate.invoke(request)
    }
}
