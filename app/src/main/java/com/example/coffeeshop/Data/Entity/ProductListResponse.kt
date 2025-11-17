package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Lớp này đại diện cho phản hồi từ API getItemsList.php
 * API này trả về result là một mảng các Product trực tiếp
 */
data class ProductListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    // result là một mảng các Product trực tiếp (không phải object có field product)
    @SerializedName("result")
    val result: List<Product>?
)

