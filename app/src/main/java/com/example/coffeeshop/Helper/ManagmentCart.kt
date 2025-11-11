package com.example.coffeeshop.Helper

import android.content.Context
import android.widget.Toast
import com.example.coffeeshop.Activity.ChangeNumberItemsListener
import com.example.coffeeshop.Data.Entity.Product


class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItems(item: Product) {
        val listItem = getListCart()

        // Kiểm tra trùng dựa trên cả title và size
        val index = listItem.indexOfFirst { it.title == item.title && it.size == item.size }

        if (index != -1) {
            // Nếu đã có cùng sản phẩm + cùng size -> cập nhật số lượng
            listItem[index].numberInCart += item.numberInCart
        } else {
            // Nếu chưa có -> thêm mới
            listItem.add(item)
        }

        tinyDB.putListObject("CartList", listItem)
        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<Product> {
        return tinyDB.getListObject<Product>("CartList", Product::class.java) ?: arrayListOf()
    }

    fun minusItem(listItems: ArrayList<Product>, position: Int, listener: ChangeNumberItemsListener) {
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }
    fun removeItem(listItems: ArrayList<Product>, position: Int, listener: ChangeNumberItemsListener) {

        listItems.removeAt(position)

        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun plusItem(listItems: ArrayList<Product>, position: Int, listener: ChangeNumberItemsListener) {
        listItems[position].numberInCart++
        tinyDB.putListObject("CartList", listItems)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listItem = getListCart()
        var fee = 0.0
        for (item in listItem) {
            fee += item.price * item.numberInCart
        }
        return fee
    }

    fun clearCart() {
        tinyDB.putListObject("CartList", ArrayList())
    }
}