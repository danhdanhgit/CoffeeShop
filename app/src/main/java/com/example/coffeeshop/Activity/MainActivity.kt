package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.coffeeshop.Adapter.CategoryAdapter
import com.example.coffeeshop.Adapter.PopularAdapter
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // Lưu trữ danh sách sản phẩm gốc để thực hiện tìm kiếm
    private var popularList: List<Product> = listOf()
    private lateinit var popularAdapter: PopularAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()
        initSearch() // Gọi hàm khởi tạo chức năng tìm kiếm
    }


    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.loadBanner() // Yêu cầu ViewModel tải dữ liệu
        viewModel.loadBanner().observe(this, Observer { list ->
            binding.progressBarBanner.visibility = View.GONE
            if (!list.isNullOrEmpty()) {
                Glide.with(this@MainActivity)
                    .load(list[0].url)
                    .into(binding.banner)
            }
        })
    }

    // Khởi tạo Category theo kiến trúc MVVM mới
    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.loadCategories() // Yêu cầu ViewModel tải dữ liệu
        viewModel.categories.observe(this, Observer { list ->
            binding.progressBarCategory.visibility = View.GONE
            if (!list.isNullOrEmpty()) {
                binding.recyclerViewCat.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerViewCat.adapter = CategoryAdapter(list)
            }
        })
    }


    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadPopular() // Yêu cầu ViewModel tải dữ liệu
        viewModel.popular.observe(this, Observer { list ->
            binding.progressBarPopular.visibility = View.GONE
            if (!list.isNullOrEmpty()) {
                this.popularList = list // Lưu lại danh sách gốc
                popularAdapter = PopularAdapter(ArrayList(list)) // Khởi tạo adapter với một danh sách có thể thay đổi
                binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)
                binding.recyclerViewPopular.adapter = popularAdapter
            }
        })
    }

    private fun initBottomMenu() {
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.btnFav.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        binding.btnOrder.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    // Khởi tạo chức năng tìm kiếm
    private fun initSearch() {
        // Khi click vào ô search hoặc button search, mở SearchActivity
        binding.edtSearch.setOnClickListener {
            val query = binding.edtSearch.text.toString()
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("search_query", query)
            startActivity(intent)
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.edtSearch.text.toString()
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("search_query", query)
            startActivity(intent)
        }

        // Vẫn giữ chức năng filter trên màn hình chính (tùy chọn)
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase().trim()
                val filteredList = if (query.isEmpty()) {
                    popularList // Nếu ô tìm kiếm trống, trả về danh sách gốc
                } else {
                    popularList.filter { product ->
                        product.title.lowercase().contains(query)
                    }
                }
                // Cập nhật adapter với danh sách đã lọc
                if (::popularAdapter.isInitialized) {
                     (popularAdapter as PopularAdapter).updateList(filteredList)
                }
            }
        })
    }
}