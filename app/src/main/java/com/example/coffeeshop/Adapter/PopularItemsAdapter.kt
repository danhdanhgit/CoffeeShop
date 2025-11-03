package com.example.coffeeshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.databinding.ViewholderPopularBinding

class PopularItemsAdapter(private val items: MutableList<ItemsModel>) : RecyclerView.Adapter<PopularItemsAdapter.ViewHolder>() {

	private lateinit var context: Context

	class ViewHolder(val binding: ViewholderPopularBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		context = parent.context
		val binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = items[position]
		holder.binding.txtTitle.text = item.title
		holder.binding.txtPrice.text = "${item.price} Đ"

		val firstImage = item.picUrl.firstOrNull()
		Glide.with(context)
			.load(firstImage)
			.into(holder.binding.pic)
	}

	override fun getItemCount(): Int = items.size

	fun updateList(newList: List<ItemsModel>) {
		items.clear()
		items.addAll(newList)
		notifyDataSetChanged()
	}
}
