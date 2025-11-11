package com.example.coffeeshop.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Activity.ItemsListActivity
import com.example.coffeeshop.Data.Entity.Category
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ViewholderCategoryBinding


class CategoryAdapter(val items: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.Viewholder>() {

    private lateinit var context: Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class Viewholder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoryAdapter.Viewholder, position: Int
    ) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        //Glide.with(holder.itemView.context)
            //.load(item.image)
            //.into(holder.binding.pic)

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, ItemsListActivity::class.java).apply {
                    putExtra("id", item.id)
                    putExtra("title", item.title)
                }
                ContextCompat.startActivity(context, intent, null)
            }, 500)
        }

        if (selectedPosition == position){
            holder.binding.titleCat.setBackgroundResource(R.drawable.dark_brown_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.white))
        } else{
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.darkBrown))
        }

    }

    override fun getItemCount(): Int = items.size
    
    fun clearSelection() {
        val previouslySelected = selectedPosition
        selectedPosition = -1
        lastSelectedPosition = -1
        if (previouslySelected != -1) {
            notifyItemChanged(previouslySelected)
        }
    }

}