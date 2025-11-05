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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.SearchAdapter
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Lấy từ khóa tìm kiếm ban đầu và thiết lập UI
        val initialQuery = intent.getStringExtra("query")
        binding.edtSearch.setText(initialQuery)


        initControl()
        setupRecyclerView()
        observeViewModel()
        initSearch()

        if (!initialQuery.isNullOrEmpty()) {
            mainViewModel.searchProducts(initialQuery)
        }
    }

    private fun initControl() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(mutableListOf())
        binding.recyclerSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
    }

    private fun observeViewModel() {
        mainViewModel.searchResults.observe(this, Observer { products ->
            binding.progressBar.visibility = View.GONE
            if (products.isNullOrEmpty()) {
                binding.txtEmpty.visibility = View.VISIBLE
                binding.recyclerSearch.visibility = View.GONE
            } else {
                binding.txtEmpty.visibility = View.GONE
                binding.recyclerSearch.visibility = View.VISIBLE
            }
            searchAdapter.updateList(products)
        })
    }

    private fun initSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                // Hiển thị loading và gọi viewmodel
                binding.progressBar.visibility = View.VISIBLE
                binding.txtEmpty.visibility = View.GONE
                mainViewModel.searchProducts(query)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
