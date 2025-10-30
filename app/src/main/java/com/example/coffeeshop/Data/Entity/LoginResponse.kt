package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Lớp này đại diện cho thông tin chi tiết của người dùng trả về từ API
 */
data class UserDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("phone")
    val phone: String
)

/**
 * Lớp này đại diện cho toàn bộ cấu trúc JSON trả về từ API đăng nhập (login.php)
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: List<UserDetail>
)
