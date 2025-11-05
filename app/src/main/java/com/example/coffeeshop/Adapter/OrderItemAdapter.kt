package com.example.coffeeshop.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Data.Entity.OrderItem
import com.example.coffeeshop.databinding.ViewholderOrderItemBinding

class OrderItemAdapter(private val items: List<OrderItem>) :
    RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderOrderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderOrderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Product name
        holder.binding.txtProductName.text = item.productTitle ?: "Sản phẩm #${item.productId}"

        // Quantity
        holder.binding.txtQuantity.text = "Số lượng: ${item.quantity}"

        // Price
        val totalPrice = item.price * item.quantity
        holder.binding.txtPrice.text = "${totalPrice.toInt()} Đ"

        // Image
        if (item.productUrl != null && item.productUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.productUrl)
                .into(holder.binding.imgProduct)
        }
    }

    override fun getItemCount(): Int = items.size
}


