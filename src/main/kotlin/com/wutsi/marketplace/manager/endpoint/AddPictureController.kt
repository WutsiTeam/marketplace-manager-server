package com.wutsi.marketplace.manager.endpoint

import com.wutsi.marketplace.manager.`delegate`.AddPictureDelegate
import com.wutsi.marketplace.manager.dto.AddPictureRequest
import com.wutsi.marketplace.manager.dto.AddPictureResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class AddPictureController(
    public val `delegate`: AddPictureDelegate
) {
    @PostMapping("/v1/pictures")
    public fun invoke(
        @Valid @RequestBody
        request: AddPictureRequest
    ): AddPictureResponse =
        delegate.invoke(request)
}
