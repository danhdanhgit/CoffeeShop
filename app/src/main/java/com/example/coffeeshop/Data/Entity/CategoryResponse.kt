package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

/**
 * Lớp này đại diện cho toàn bộ đối tượng JSON trả về từ API getCategory.
 */
data class CategoryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    // `result` là một danh sách các đối tượng Category
    @SerializedName("result")
    val result: List<Category>? // Để là nullable để an toàn
)
