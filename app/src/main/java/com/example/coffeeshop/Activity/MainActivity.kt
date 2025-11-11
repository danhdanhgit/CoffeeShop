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
import com.example.coffeeshop.Adapter.PopularItemsAdapter
import com.example.coffeeshop.Helper.ManagmentCart
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	lateinit var binding: ActivityMainBinding
	private val viewModel: MainViewModel by viewModels()

	private lateinit var popularItemsAdapter: PopularItemsAdapter
	private lateinit var managmentCart: ManagmentCart


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		managmentCart = ManagmentCart(this)

		initBanner()
		initCategory()
		initPopular()
		initBottomMenu()
		initSearch()
		updateCartBadge()
	}




	private fun initBanner() {
		binding.progressBarBanner.visibility = View.VISIBLE
		viewModel.loadBanner()
		viewModel.loadBanner().observe(this, Observer { list ->
			binding.progressBarBanner.visibility = View.GONE
			if (!list.isNullOrEmpty()) {
				Glide.with(this@MainActivity)
					.load(list[0].url)
					.into(binding.banner)
			}
		})
	}


	private fun initCategory() {
		binding.progressBarCategory.visibility = View.VISIBLE
		viewModel.loadCategories()
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
		viewModel.loadPopular()
		viewModel.loadPopular().observe(this, Observer { list ->
			binding.progressBarPopular.visibility = View.GONE
			if (!list.isNullOrEmpty()) {
				popularItemsAdapter = PopularItemsAdapter(ArrayList(list))
				binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)
				binding.recyclerViewPopular.adapter = popularItemsAdapter
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


	private fun initSearch() {
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
	}

	private fun updateCartBadge() {
		val count = managmentCart.getTotalQuantity()
		val badge = binding.txtCartBadge

		if (count > 0) {
			badge.text = count.toString()
			badge.visibility = View.VISIBLE
		} else {
			badge.visibility = View.GONE
		}
	}


	override fun onResume() {
		super.onResume()
		//Cập nhật lại badge mỗi khi quay lại màn hình chính
		updateCartBadge()

		//Reset chọn danh mục
		(binding.recyclerViewCat.adapter as? CategoryAdapter)?.clearSelection()
	}
}