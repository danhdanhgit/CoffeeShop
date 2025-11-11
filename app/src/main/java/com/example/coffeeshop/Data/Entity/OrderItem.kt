package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Entity đại diện cho một item trong đơn hàng (order_items table)
 */
data class OrderItem(
    @SerializedName("order_item_id")
    val orderItemId: Int,

    @SerializedName("order_id")
    val orderId: Int,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("qty")
    val quantity: Int,

    @SerializedName("size")
    val size: String,

    @SerializedName("price")
    val price: Double,

    // Thông tin product để hiển thị (từ JOIN query hoặc fetch riêng)
    @SerializedName("product_title")
    val productTitle: String? = null,

    @SerializedName("product_url")
    val productUrl: String? = null
) : Serializable


