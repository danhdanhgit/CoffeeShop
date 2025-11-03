package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Response từ API lấy danh sách đơn hàng
 */
data class OrderResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: List<Order>
)


