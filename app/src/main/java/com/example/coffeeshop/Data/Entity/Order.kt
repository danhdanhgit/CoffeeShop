package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Entity đại diện cho một đơn hàng (orders table)
 */
data class Order(
    @SerializedName("order_id")
    val orderId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("customer_name")
    val customerName: String,

    @SerializedName("customer_phone")
    val customerPhone: String,

    @SerializedName("customer_address")
    val customerAddress: String,

    @SerializedName("total")
    val total: Float,

    @SerializedName("status")
    val status: String, // "pending", "completed", "cancelled"

    @SerializedName("created_at")
    val createdAt: String,

    // Danh sách items trong đơn hàng (từ JOIN query)
    @SerializedName("items")
    val items: List<OrderItem>? = null
) : Serializable


