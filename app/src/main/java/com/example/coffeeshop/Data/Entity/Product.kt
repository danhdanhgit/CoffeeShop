package com.example.coffeeshop.Data.Entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Lớp này đại diện cho một đối tượng sản phẩm (item) duy nhất.
 */
data class Product(
    @SerializedName("product_id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("extra")
    val extra: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("url")
    val picUrl: String,

    @SerializedName("rating")
    val rating: Float,

    @SerializedName("category_id")
    val categoryId: Int,

    var numberInCart: Int = 0
) : Serializable
