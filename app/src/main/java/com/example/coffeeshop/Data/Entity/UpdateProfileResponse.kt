package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: Any? = null
)
