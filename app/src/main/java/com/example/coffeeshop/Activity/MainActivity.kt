package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.coffeeshop.Adapter.CategoryAdapter
import com.example.coffeeshop.Adapter.PopularAdapter
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    // Lấy ViewModel bằng delegate để ViewModelProvider tạo AndroidViewModel với Application
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()
    }
    //Hien thi banner
    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.loadBanner().observe(this@MainActivity) { list ->
            if (list.isNotEmpty()) {
                Glide.with(this@MainActivity)
                    .load(list[0].url)
                    .into(binding.banner)
            }
            binding.progressBarBanner.visibility = View.GONE
        }
    }
    //Hien thi cac Category
    private fun initCategory(){
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.loadCategories().observe(this@MainActivity) { list ->
            binding.recyclerViewCat.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)


            binding.recyclerViewCat.adapter = CategoryAdapter(list)
            binding.progressBarCategory.visibility = View.GONE
        }
    }

    private fun initPopular(){
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadPopular().observe(this@MainActivity) { list ->
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewPopular.adapter = PopularAdapter(list)
            binding.progressBarPopular.visibility = View.GONE
        }
    }

    private fun initBottomMenu() {
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}