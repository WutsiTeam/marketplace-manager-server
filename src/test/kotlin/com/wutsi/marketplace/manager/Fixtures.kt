package com.wutsi.marketplace.manager

import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.enums.StoreStatus
import com.wutsi.membership.access.dto.Account
import com.wutsi.membership.access.dto.Phone
import com.wutsi.membership.access.enums.AccountStatus

object Fixtures {
    fun createAccount(
        id: Long = -1,
        status: AccountStatus = AccountStatus.ACTIVE,
        business: Boolean = false,
        businessId: Long? = null,
        storeId: Long? = null,
        country: String = "CM",
        phoneNumber: String = "+237670000010",
        displayName: String = "Ray Sponsible"
    ) = Account(
        id = id,
        displayName = displayName,
        status = status.name,
        business = business,
        country = country,
        businessId = businessId,
        storeId = storeId,
        phone = Phone(
            number = phoneNumber,
            country = country
        )
    )

    fun createStore(
        id: Long = -1,
        accountId: Long = -1,
        status: StoreStatus = StoreStatus.ACTIVE,
        productCount: Int = 0
    ) = Store(
        id = id,
        accountId = accountId,
        status = status.name,
        productCount = productCount
    )

    fun createProduct(id: Long = -1, storeId: Long = -1, pictures: List<PictureSummary> = emptyList()) = Product(
        id = id,
        storeId = storeId,
        pictures = pictures
    )

    fun createPictureSummary() = PictureSummary()
}
