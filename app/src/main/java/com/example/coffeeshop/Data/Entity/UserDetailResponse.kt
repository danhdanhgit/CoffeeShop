package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Response từ API lấy thông tin chi tiết user
 */
data class UserDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: List<UserDetail>?
)


