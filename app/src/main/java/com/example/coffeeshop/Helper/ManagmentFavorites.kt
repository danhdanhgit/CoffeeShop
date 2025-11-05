package com.example.coffeeshop.Helper

import android.content.Context
import com.example.coffeeshop.Data.Entity.Product

/**
 * Quản lý danh sách sản phẩm yêu thích
 * Lưu trữ local bằng TinyDB (vì database chưa có bảng favorites)
 */
class ManagmentFavorites(val context: Context) {

    private val tinyDB = TinyDB(context)


    fun addFavorite(item: Product) {
        val listItem = getFavoritesList()
        val existAlready = listItem.any { it.id == item.id }

        if (!existAlready) {
            listItem.add(item)
            tinyDB.putListObject("FavoritesList", listItem)
        }
    }


    fun removeFavorite(productId: Int) {
        val listItem = getFavoritesList()
        listItem.removeAll { it.id == productId }
        tinyDB.putListObject("FavoritesList", listItem)
    }


    fun isFavorite(productId: Int): Boolean {
        val listItem = getFavoritesList()
        return listItem.any { it.id == productId }
    }


    fun getFavoritesList(): ArrayList<Product> {
        return try {
            tinyDB.getListObject<Product>("FavoritesList", Product::class.java) ?: arrayListOf()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    /**
     * Toggle favorite (thêm nếu chưa có, xóa nếu đã có)
     */
    fun toggleFavorite(item: Product) {
        if (isFavorite(item.id)) {
            removeFavorite(item.id)
        } else {
            addFavorite(item)
        }
    }
}


