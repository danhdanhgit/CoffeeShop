package com.example.coffeeshop.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.coffeeshop.Data.Entity.Category
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.Repository.MainRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MainRepository(getApplication())

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<Category>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    fun loadItemsByCategory(categoryId: Int): LiveData<MutableList<Product>> {
        return repository.loadItemsByCategory(categoryId)
    }
}
