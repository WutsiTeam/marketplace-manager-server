package com.wutsi.marketplace.manager.event

enum class EventURN(val urn: String) {
    STORE_ENABLED("urn:wutsi:event:marketplace-manager:store-enabled"),
    STORE_SUSPENDED("urn:wutsi:event:marketplace-manager:store-suspended")
}
