package com.example.coffeeshop.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.SearchAdapter
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.ViewModel.SearchResultState
import com.example.coffeeshop.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialQuery = intent.getStringExtra("query")
        binding.edtSearch.setText(initialQuery)

        setupRecyclerView()
        observeViewModel()
        initSearchListeners()

        if (!initialQuery.isNullOrEmpty()) {
            performSearch(initialQuery)
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
        mainViewModel.searchResults.observe(this) { state ->
            when (state) {
                is SearchResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerSearch.visibility = View.GONE
                    binding.txtEmpty.visibility = View.GONE
                }
                is SearchResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (state.products.isEmpty()) {
                        binding.recyclerSearch.visibility = View.GONE
                        binding.txtEmpty.visibility = View.VISIBLE
                        binding.txtEmpty.text = "Không tìm thấy sản phẩm nào"
                    } else {
                        binding.recyclerSearch.visibility = View.VISIBLE
                        binding.txtEmpty.visibility = View.GONE
                        searchAdapter.updateList(state.products)
                    }
                }
                is SearchResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerSearch.visibility = View.GONE
                    binding.txtEmpty.visibility = View.VISIBLE
                    val errorMessage = "Lỗi từ Server: ${state.message}"
                    binding.txtEmpty.text = errorMessage
                    // IN LỖI RA LOGCAT ĐỂ DỄ DEBUG
                    Log.e("SearchActivity", errorMessage)
                }
            }
        }
    }

    private fun initSearchListeners() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun performSearch(query: String) {
        val trimmedQuery = query.trim()
        mainViewModel.searchProducts(trimmedQuery)
    }
}
