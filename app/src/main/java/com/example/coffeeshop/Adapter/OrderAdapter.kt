package com.example.coffeeshop.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.Activity.OrderDetailActivity
import com.example.coffeeshop.Data.Entity.Order
import com.example.coffeeshop.databinding.ViewholderOrderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]


        holder.binding.txtOrderId.text = "Đơn hàng #${order.orderId}"


        holder.binding.txtStatus.text = getStatusText(order.status)
        holder.binding.txtStatus.setBackgroundResource(getStatusBackground(order.status))


        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(order.createdAt)
            holder.binding.txtOrderDate.text = "Ngày đặt: ${outputFormat.format(date ?: System.currentTimeMillis())}"
        } catch (e: Exception) {
            holder.binding.txtOrderDate.text = "Ngày đặt: ${order.createdAt}"
        }


        holder.binding.txtTotal.text = "${order.total.toInt()} VND"


        holder.binding.txtAddress.text = "Địa chỉ: ${order.customerAddress}"

        // Order Items
        if (order.items != null && order.items.isNotEmpty()) {
            val itemsAdapter = OrderItemAdapter(order.items)
            holder.binding.recyclerViewOrderItems.layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)
            holder.binding.recyclerViewOrderItems.adapter = itemsAdapter
        }

        holder.itemView.setOnClickListener {
            val ctx = holder.itemView.context
            val intent = Intent(ctx, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orders.size

    private fun getStatusText(status: String): String {
        return when (status.lowercase()) {
            "pending" -> "Đang xử lý"
            "completed" -> "Hoàn thành"
            "cancelled" -> "Đã hủy"
            else -> status
        }
    }

    private fun getStatusBackground(status: String): Int {
        return when (status.lowercase()) {
            "pending" -> com.example.coffeeshop.R.drawable.orange_bg
            "completed" -> com.example.coffeeshop.R.drawable.dark_brown_bg
            "cancelled" -> android.R.color.darker_gray
            else -> com.example.coffeeshop.R.drawable.orange_bg
        }
    }
}


