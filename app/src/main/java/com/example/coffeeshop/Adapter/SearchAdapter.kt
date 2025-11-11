package com.example.coffeeshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Activity.DetailActivity
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.databinding.ViewholderItemPicLeftBinding


class SearchAdapter(private var items: MutableList<Product>) :
    RecyclerView.Adapter<SearchAdapter.Viewholder>() {


    private lateinit var context: Context

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        context = parent.context
        val binding = ViewholderItemPicLeftBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.txtTitle.text = item.title
        holder.binding.txtPrice.text = "${item.price.toString()} VND"
        holder.binding.ratingBar.rating = item.rating
        Glide.with(context)
            .load(item.picUrl)
            .into(holder.binding.picMain)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("product_id", item.id)
            context.startActivity(intent)
        }
    }

    fun updateList(newList: List<Product>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class Viewholder(val binding: ViewholderItemPicLeftBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}