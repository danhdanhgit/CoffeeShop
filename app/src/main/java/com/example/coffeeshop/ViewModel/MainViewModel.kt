package com.example.coffeeshop.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.CategoryModel
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.Repository.MainRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // Use application as Context for repository
    private val repository = MainRepository(getApplication())

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadItems(categoryId: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadItemCategory(categoryId)
    }
}