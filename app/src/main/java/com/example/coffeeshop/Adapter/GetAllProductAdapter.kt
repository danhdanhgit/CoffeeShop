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
import com.example.coffeeshop.databinding.ViewholderItemPicRightBinding

class GetAllProductAdapter(val items: List<Product>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        companion object {
            private const val TYPE_ITEM1 = 0
            private const val TYPE_ITEM2 = 1
        }
    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_ITEM1 else TYPE_ITEM2
    }

    class ViewholderItem1 (val binding: ViewholderItemPicRightBinding):
    RecyclerView.ViewHolder(binding.root)

    class ViewholderItem2 (val binding: ViewholderItemPicLeftBinding):
    RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        context = parent.context
        return when(viewType) {
            TYPE_ITEM1 -> {
                val binding = ViewholderItemPicRightBinding.inflate(
                    LayoutInflater.from(context),
                    parent, false
                )
                ViewholderItem1(binding)
            }
            TYPE_ITEM2 -> {
                val binding = ViewholderItemPicLeftBinding.inflate(
                    LayoutInflater.from(context),
                    parent, false
                )
                ViewholderItem2(binding)
            }

            else -> throw IllegalArgumentException("Invalidview type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = items[position]
            when (holder) {
                is ViewholderItem1 -> bindRightViewHolder(holder, item)

                is ViewholderItem2  -> bindLeftViewHolder(holder, item)
                }
        }

    private fun bindRightViewHolder(holder: ViewholderItem1, item: Product) {
        holder.binding.apply {
            txtTitle.text = item.title
            txtPrice.text = "${item.price} VND"
            ratingBar.rating = item.rating

            Glide.with(context)
                .load(item.picUrl)
                .into(picMain)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("product_id", item.id)
                context.startActivity(intent)
            }
        }

    }

    private fun bindLeftViewHolder(holder: ViewholderItem2, item: Product) {
        holder.binding.apply {
            txtTitle.text = item.title
            txtPrice.text = "${item.price} VND"
            ratingBar.rating = item.rating

            Glide.with(context)
                .load(item.picUrl)
                .into(picMain)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("product_id", item.id)
                context.startActivity(intent)
            }
        }

    }
}