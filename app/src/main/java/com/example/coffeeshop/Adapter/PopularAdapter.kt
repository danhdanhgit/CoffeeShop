package com.example.coffeeshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Activity.DetailActivity
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.databinding.ViewholderPopularBinding

// Sửa lại để chấp nhận một danh sách có thể thay đổi và sử dụng Product
class PopularAdapter(private var items: MutableList<Product>) : RecyclerView.Adapter<PopularAdapter.Viewholder>() {

    private lateinit var context: Context

    class Viewholder(val binding: ViewholderPopularBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        holder.binding.txtTitle.text = item.title
        holder.binding.txtPrice.text = "${item.price} Đ"

        Glide.with(context)
            .load(item.picUrl)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            // Gửi product_id thay vì cả object để DetailActivity tự tải dữ liệu mới nhất
            intent.putExtra("product_id", item.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    // Hàm để cập nhật danh sách và thông báo cho adapter
    fun updateList(newList: List<Product>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged() // Thông báo cho RecyclerView cập nhật lại giao diện
    }
}