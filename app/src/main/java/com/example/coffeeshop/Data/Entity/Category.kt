package com.example.coffeeshop.Data.Entity


import com.google.gson.annotations.SerializedName


data class Category(

    @SerializedName("category_id")
    val id: Int,

    @SerializedName("title")
    val title: String
)
