package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeeshop.Adapter.PopularAdapter
import com.example.coffeeshop.Helper.ManagmentFavorites
import com.example.coffeeshop.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var managmentFavorites: ManagmentFavorites
    private lateinit var favoritesAdapter: PopularAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentFavorites = ManagmentFavorites(this)

        initControl()
        loadFavorites()
    }

    private fun initControl() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadFavorites() {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtEmpty.visibility = View.GONE

        val favoritesList = managmentFavorites.getFavoritesList()

        binding.progressBar.visibility = View.GONE

        if (favoritesList.isEmpty()) {
            binding.txtEmpty.visibility = View.VISIBLE
            binding.recyclerViewFavorites.visibility = View.GONE
        } else {
            binding.txtEmpty.visibility = View.GONE
            binding.recyclerViewFavorites.visibility = View.VISIBLE

            // Sử dụng PopularAdapter nhưng cần đảm bảo click vào sản phẩm sẽ mở DetailActivity
            // PopularAdapter đã có sẵn chức năng này nên không cần thay đổi
            favoritesAdapter = PopularAdapter(ArrayList(favoritesList))
            binding.recyclerViewFavorites.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewFavorites.adapter = favoritesAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload khi quay lại từ DetailActivity (có thể đã xóa favorite)
        loadFavorites()
    }
}

