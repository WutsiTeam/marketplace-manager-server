package com.wutsi.marketplace.manager

import com.wutsi.enums.AccountStatus
import com.wutsi.enums.DiscountType
import com.wutsi.enums.MeetingProviderType
import com.wutsi.enums.ProductStatus
import com.wutsi.enums.ProductType
import com.wutsi.enums.StoreStatus
import com.wutsi.marketplace.access.dto.CategorySummary
import com.wutsi.marketplace.access.dto.Discount
import com.wutsi.marketplace.access.dto.DiscountSummary
import com.wutsi.marketplace.access.dto.FileSummary
import com.wutsi.marketplace.access.dto.MeetingProviderSummary
import com.wutsi.marketplace.access.dto.Offer
import com.wutsi.marketplace.access.dto.OfferPrice
import com.wutsi.marketplace.access.dto.OfferSummary
import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.ProductSummary
import com.wutsi.marketplace.access.dto.ReservationSummary
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.dto.StoreSummary
import com.wutsi.membership.access.dto.Account
import com.wutsi.membership.access.dto.Phone
import java.time.OffsetDateTime
import java.time.ZoneOffset

object Fixtures {
    fun createAccount(
        id: Long = -1,
        status: AccountStatus = AccountStatus.ACTIVE,
        business: Boolean = false,
        businessId: Long? = null,
        storeId: Long? = null,
        country: String = "CM",
        phoneNumber: String = "+237670000010",
        displayName: String = "Ray Sponsible",
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
            country = country,
        ),
    )

    fun createStore(
        id: Long = -1,
        accountId: Long = -1,
        status: StoreStatus = StoreStatus.ACTIVE,
        productCount: Int = 0,
    ) = Store(
        id = id,
        accountId = accountId,
        status = status.name,
        productCount = productCount,
    )

    fun createStoreSummary(
        id: Long = -1,
        accountId: Long = -1,
        businessId: Long = -1,
        status: StoreStatus = StoreStatus.ACTIVE,
    ) = StoreSummary(
        id = id,
        accountId = accountId,
        businessId = businessId,
        status = status.name,
    )

    fun createFileSummary(
        id: Long,
    ) = FileSummary(
        id = id,
        url = "https://www.img.com/$id.png",
        name = "$id.png",
        contentType = "image/png",
        contentSize = 10000,
        created = OffsetDateTime.of(2020, 1, 1, 10, 30, 0, 0, ZoneOffset.UTC),
    )

    fun createProduct(
        id: Long = -1,
        storeId: Long = -1,
        quantity: Int? = 11,
        pictures: List<PictureSummary> = emptyList(),
        type: ProductType = ProductType.PHYSICAL_PRODUCT,
        event: com.wutsi.marketplace.access.dto.Event? = null,
        files: List<FileSummary> = emptyList(),
    ) = Product(
        id = id,
        store = StoreSummary(
            id = storeId,
            accountId = -1,
            currency = "XAF",
        ),
        pictures = pictures,
        summary = "This is a summary",
        description = "This is the description",
        price = 100000L,
        quantity = quantity,
        status = ProductStatus.DRAFT.name,
        type = type.name,
        thumbnail = if (pictures.isEmpty()) null else pictures[0],
        currency = "XAF",
        title = "This is the title",
        event = event,
        category = CategorySummary(
            id = 1,
            title = "Art",
        ),
        files = files,
    )

    fun createProductSummary(id: Long = -1) = ProductSummary(
        id = id,
    )

    fun createPictureSummary(id: Long = -1) = PictureSummary(
        id = id,
        url = "https://img.com/$id.png",
    )

    fun createReservationSummary(id: Long) = ReservationSummary(
        id = id,
    )

    fun createMeetingProviderSummary(id: Long, type: MeetingProviderType = MeetingProviderType.ZOOM) =
        MeetingProviderSummary(
            id = id,
            logoUrl = "https://img.com/$id.png",
            name = "NAME-$id",
            type = type.name,
        )

    fun createEvent(
        starts: OffsetDateTime = OffsetDateTime.of(2020, 1, 1, 10, 30, 0, 0, ZoneOffset.UTC),
        ends: OffsetDateTime = OffsetDateTime.of(2020, 1, 1, 15, 30, 0, 0, ZoneOffset.UTC),
        meetingProvider: MeetingProviderSummary? = null,
    ) = com.wutsi.marketplace.access.dto.Event(
        online = true,
        meetingPassword = "123456",
        meetingId = "1234567890",
        meetingJoinUrl = "https://us04.zoom.us/j/12345678",
        starts = starts,
        ends = ends,
        meetingProvider = meetingProvider,
    )

    fun createMeetingProviderSummary() = MeetingProviderSummary(
        id = 1000,
        type = MeetingProviderType.ZOOM.name,
        name = "Zoom",
        logoUrl = "https://prod-wutsi.s3.amazonaws.com/static/marketplace-access-server/meeting-providers/zoom.png",
    )

    fun createDiscount(
        id: Long = 1000,
        storeId: Long = 1,
        rate: Int = 10,
        starts: OffsetDateTime = OffsetDateTime.now(),
        ends: OffsetDateTime = OffsetDateTime.now().plusDays(10),
        allProducts: Boolean = false,
        productIds: List<Long> = listOf(100L, 102L, 100000L),
    ) = Discount(
        id = id,
        storeId = storeId,
        name = "FIN$rate",
        rate = rate,
        starts = starts,
        ends = ends,
        allProducts = allProducts,
        productIds = productIds,
        type = DiscountType.COUPON.name,
    )

    fun createDiscountSummary(
        id: Long = 1000,
        storeId: Long = 1,
        rate: Int = 10,
        starts: OffsetDateTime = OffsetDateTime.now(),
        ends: OffsetDateTime = OffsetDateTime.now().plusDays(10),
    ) = DiscountSummary(
        id = id,
        storeId = storeId,
        name = "FIN$rate",
        rate = rate,
        starts = starts,
        ends = ends,
        type = DiscountType.COUPON.name,
    )

    fun createOfferPrice(productId: Long) = OfferPrice(
        productId = productId,
    )

    fun createOfferSummary(
        productId: Long,
    ) = OfferSummary(
        product = createProductSummary(productId),
        price = createOfferPrice(productId),
    )

    fun createOffer(
        productId: Long,
    ) = Offer(
        product = createProduct(productId),
        price = createOfferPrice(productId),
    )
}
