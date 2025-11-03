package com.example.coffeeshop.Data.Entity


import com.google.gson.annotations.SerializedName


data class Category(

	@SerializedName(value = "category_id", alternate = ["id", "categoryId"])
	val id: Int,

	@SerializedName(value = "title", alternate = ["name", "category_name"])
	val title: String
)
