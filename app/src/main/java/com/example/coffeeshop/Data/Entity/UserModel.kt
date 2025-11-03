package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Lớp này đại diện cho cấu trúc JSON trả về từ API đăng ký (register.php).
 * Ví dụ: {"success": true, "message": "Thành công"}
 */
data class UserModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)
