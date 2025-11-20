package com.example.coffeeshop.Helper

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.Entity.Product

/**
 * Quản lý danh sách sản phẩm yêu thích
 * Lưu trữ local bằng TinyDB (vì database chưa có bảng favorites)
 */
class ManagmentFavorites(val context: Context) {

    private val tinyDB = TinyDB(context)

    private fun getFavoritesKey(userId: String): String {
        if (userId.isBlank()) {
            return "FavoritesList_Guest"
        }
        return "FavoritesList_$userId"
    }

    fun addFavorite(item: Product, userId: String) {
        val key = getFavoritesKey(userId)
        val listItem = getFavoritesList(userId)
        val existAlready = listItem.any { it.id == item.id }

        if (!existAlready) {
            listItem.add(item)
            tinyDB.putListObject(key, listItem)
        }
    }


    fun removeFavorite(productId: Int, userId: String) {
        val key = getFavoritesKey(userId)
        val listItem = getFavoritesList(userId)
        listItem.removeAll { it.id == productId }
        tinyDB.putListObject("FavoritesList", listItem)
    }


    fun isFavorite(productId: Int, userId: String): Boolean {
        val listItem = getFavoritesList(userId)
        return listItem.any { it.id == productId }
    }


    fun getFavoritesList(userId: String): ArrayList<Product> {
        val key = getFavoritesKey(userId)
        return try {
            tinyDB.getListObject<Product>(key, Product::class.java) ?: arrayListOf()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    /**
     * Toggle favorite (thêm nếu chưa có, xóa nếu đã có)
     */
    fun toggleFavorite(item: Product, userId: String) {
        if (isFavorite(item.id, userId)) {
            removeFavorite(item.id, userId)
        } else {
            addFavorite(item, userId)
        }
    }
}


