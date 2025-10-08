package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityDetailBinding
import com.example.coffeeshop.databinding.ActivityMainBinding


class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        bundle()
        initSizeList()

    }

    private fun initSizeList() {
        binding.apply {
            btnSmall.setOnClickListener {
                btnSmall.setBackgroundResource(R.drawable.stroke_brown_bg)
                btnMedium.setBackgroundResource(0)
                btnLarge.setBackgroundResource(0)
            }
            btnMedium.setOnClickListener {
                btnSmall.setBackgroundResource(0)
                btnMedium.setBackgroundResource(R.drawable.stroke_brown_bg)
                btnLarge.setBackgroundResource(0)
            }
            btnLarge.setOnClickListener {
                btnSmall.setBackgroundResource(0)
                btnMedium.setBackgroundResource(0)
                btnLarge.setBackgroundResource(R.drawable.stroke_brown_bg)
            }
        }
    }

    private fun bundle() {
        binding.apply {
            item = intent.getSerializableExtra("object") as ItemsModel

            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(binding.picMain)

            txtTitle.text = item.title
            txtDescription.text = item.description
            txtPrice.text = "${item.price} Đ"
            txtRating.text = item.rating.toString()

            btnAddToCart.setOnClickListener {
                item.numberInCart = Integer.valueOf(
                    txtNumberItem.text.toString()
                )
                managmentCart.insertItems(item)
            }

            btnBack.setOnClickListener {
                finish()
            }

            btnPlus.setOnClickListener {
                txtNumberItem.text = (item.numberInCart + 1).toString()
                item.numberInCart++
            }

            btnMinus.setOnClickListener {
                if (item.numberInCart > 0){
                    txtNumberItem.text = (item.numberInCart - 1).toString()
                    item.numberInCart--
                }
            }
        }
    }
}