package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.ItemsListCategoryAdapter
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemsListBinding
    private val viewModel: MainViewModel by viewModels()
    private var id: String = ""
    private var title: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        initList()
    }

    private fun initList() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            viewModel.loadItems(id).observe(this@ItemsListActivity, Observer {
                rvListView.layoutManager =
                    LinearLayoutManager(this@ItemsListActivity,
                        LinearLayoutManager.VERTICAL, false)
                rvListView.adapter = ItemsListCategoryAdapter(it)
                progressBar.visibility = View.GONE
            })
            btnBack.setOnClickListener {
                finish() }
        }
    }

    private fun getBundle() {
        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""

        binding.txtcategory.text = title
    }
}
