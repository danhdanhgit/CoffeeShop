package com.example.coffeeshop.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeeshop.Adapter.PopularAdapter
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var searchAdapter: PopularAdapter
    private var allProductsList: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initControl()
        loadAllProducts()
        initSearch()
    }

    private fun initControl() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadAllProducts() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadAllProducts()
        viewModel.allProducts.observe(this, Observer { products ->
            binding.progressBar.visibility = View.GONE
            allProductsList = products
            if (products.isNotEmpty()) {
                searchAdapter = PopularAdapter(ArrayList(products))
                binding.recyclerSearch.layoutManager = GridLayoutManager(this, 2)
                binding.recyclerSearch.adapter = searchAdapter
            }
        })
    }

    private fun initSearch() {
        // Lấy query từ intent (nếu có)
        val queryFromIntent = intent.getStringExtra("search_query") ?: ""
        if (queryFromIntent.isNotEmpty()) {
            binding.edtSearch.setText(queryFromIntent)
            performSearch(queryFromIntent)
        }

        // Xử lý tìm kiếm khi người dùng nhập
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                performSearch(query)
            }
        })
    }

    private fun performSearch(query: String) {
        val filteredList = if (query.isEmpty()) {
            allProductsList
        } else {
            allProductsList.filter { product ->
                product.title.lowercase().contains(query.lowercase()) ||
                product.description.lowercase().contains(query.lowercase())
            }
        }

        if (filteredList.isEmpty() && query.isNotEmpty()) {
            binding.txtEmpty.visibility = View.VISIBLE
            binding.recyclerSearch.visibility = View.GONE
        } else {
            binding.txtEmpty.visibility = View.GONE
            binding.recyclerSearch.visibility = View.VISIBLE

            if (::searchAdapter.isInitialized) {
                searchAdapter.updateList(filteredList)
            } else if (filteredList.isNotEmpty()) {
                searchAdapter = PopularAdapter(ArrayList(filteredList))
                binding.recyclerSearch.layoutManager = GridLayoutManager(this, 2)
                binding.recyclerSearch.adapter = searchAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload products khi quay lại
        if (allProductsList.isEmpty()) {
            loadAllProducts()
        }
    }
}
