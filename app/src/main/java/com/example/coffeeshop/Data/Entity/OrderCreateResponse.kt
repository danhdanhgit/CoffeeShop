package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

data class OrderCreateResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreateOrderResult?
) {
    data class CreateOrderResult(
        @SerializedName("order_id") val orderId: Int?
    )
}
