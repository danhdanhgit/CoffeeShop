package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Lớp này đại diện cho toàn bộ phản hồi từ API lấy danh sách sản phẩm.
 */
data class ProductResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: ProductResult?
)
