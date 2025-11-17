package com.example.coffeeshop.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.GetAllProductAdapter
import com.example.coffeeshop.R
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityGetAllProductBinding

class GetAllProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetAllProductBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetAllProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.apply {
            rvGetAll.layoutManager =
                        LinearLayoutManager(this@GetAllProductActivity, LinearLayoutManager.VERTICAL, false)
            progressBar.visibility = View.VISIBLE

            viewModel.allProducts.observe(this@GetAllProductActivity, Observer {products ->
                // Dữ liệu đã về
                binding.progressBar.visibility = View.GONE

                if (products != null && products.isNotEmpty()) {
                    //lấy danh sách sản phẩm
                    binding.rvGetAll.adapter = GetAllProductAdapter(products)
                } else {
                    //không có sản phẩm
                    Log.d("GetAllProductActivity", "Không nhận được sản phẩm nào hoặc danh sách đang trống")
                }
            })
            viewModel.loadAllProducts()

            binding.btnBack.setOnClickListener {
                finish()
            }
        }
    }
}