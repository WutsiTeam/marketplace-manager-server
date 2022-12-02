package com.wutsi.marketplace.manager

import com.wutsi.enums.AccountStatus
import com.wutsi.enums.ProductStatus
import com.wutsi.enums.StoreStatus
import com.wutsi.marketplace.access.dto.CategorySummary
import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.ProductSummary
import com.wutsi.marketplace.access.dto.ReservationSummary
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.dto.StoreSummary
import com.wutsi.membership.access.dto.Account
import com.wutsi.membership.access.dto.Phone

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

    fun createProduct(
        id: Long = -1,
        storeId: Long = -1,
        quantity: Int? = 11,
        pictures: List<PictureSummary> = emptyList()
    ) = Product(
        id = id,
        store = StoreSummary(
            id = storeId,
            accountId = -1,
            currency = "XAF"
        ),
        pictures = pictures,
        summary = "This is a summary",
        description = "This is the description",
        price = 100000L,
        comparablePrice = 150000L,
        quantity = quantity,
        status = ProductStatus.DRAFT.name,
        thumbnail = if (pictures.isEmpty()) null else pictures[0],
        currency = "XAF",
        title = "This is the title",
        category = CategorySummary(
            id = 1,
            title = "Art"
        )
    )

    fun createProductSummary(id: Long = -1) = ProductSummary(
        id = id
    )

    fun createPictureSummary(id: Long = -1) = PictureSummary(
        id = id,
        url = "https://img.com/$id.png"
    )

    fun createReservationSummary(id: Long) = ReservationSummary(
        id = id
    )
}
