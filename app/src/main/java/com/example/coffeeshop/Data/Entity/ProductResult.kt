package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName

data class ProductResult(
    @SerializedName("product")
    val product: Product,

    @SerializedName("images")
    val images: List<String>,

    @SerializedName("videos")
    val videos: List<String>
)