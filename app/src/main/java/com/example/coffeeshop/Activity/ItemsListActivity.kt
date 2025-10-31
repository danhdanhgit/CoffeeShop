package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.ItemsListCategoryAdapter


import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsListBinding
    private val viewModel: MainViewModel by viewModels()

    // Giữ thông tin của category
    private var categoryId: Int = 0
    private var categoryName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy dữ liệu được truyền từ activity trước
        getIntentData()

        // Khởi tạo RecyclerView và bắt đầu lấy dữ liệu
        initItemsList()
    }

    private fun getIntentData() {
        // Lấy ID và Tên của category từ intent. Mặc định là 0 và "" nếu không tìm thấy.
        categoryId = intent.getIntExtra("id", 0)
        categoryName = intent.getStringExtra("title") ?: ""

        // Gán tên category cho TextView
        binding.txtcategory.text = categoryName
    }

    private fun initItemsList() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            // 1. Yêu cầu ViewModel bắt đầu tải dữ liệu
            viewModel.loadItemsByCategory(categoryId)

            // 2. Lắng nghe thuộc tính public để nhận kết quả
            viewModel.itemsByCategory.observe(this@ItemsListActivity, Observer { products ->
                // Ẩn thanh tiến trình sau khi có kết quả
                progressBar.visibility = View.GONE

                // Kiểm tra xem danh sách có dữ liệu hay không
                if (!products.isNullOrEmpty()) {
                    rvListView.layoutManager =
                        LinearLayoutManager(this@ItemsListActivity, LinearLayoutManager.VERTICAL, false)
                    // Cập nhật adapter với dữ liệu mới
                    rvListView.adapter = ItemsListCategoryAdapter(products)
                }
            })

            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}
