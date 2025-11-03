package com.example.coffeeshop.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.Entity.Category
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.Repository.MainRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MainRepository(application)
    private val compositeDisposable = CompositeDisposable()


    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _itemsByCategory = MutableLiveData<List<Product>>()
    val itemsByCategory: LiveData<List<Product>> = _itemsByCategory

    private val _productDetail = MutableLiveData<Product?>()
    val productDetail: LiveData<Product?> = _productDetail

    // --- Các hàm để gọi từ Activity/Fragment (Tất cả đều trả về Unit) ---

    fun loadCategories(): LiveData<List<Category>> {
        compositeDisposable.add(
            repository.loadCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        // Post danh sách (hoặc danh sách rỗng nếu null) để tránh crash
                        _categories.postValue(response.result ?: emptyList())
                    } else {
                        Log.w("MainViewModel", "loadCategories was not successful")
                        _categories.postValue(emptyList())
                    }
                }, { error ->
                    Log.e("MainViewModel", "loadCategories error: " + error.message)
                    _categories.postValue(emptyList())
                })
        )
        return categories
    }

    fun loadItemsByCategory(categoryId: Int) {
        compositeDisposable.add(
            repository.loadItemsByCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success){
                        _itemsByCategory.postValue(response.result ?: emptyList())
                    } else {
                         Log.w("MainViewModel", "loadItemsByCategory was not successful")
                        _itemsByCategory.postValue(emptyList())
                    }
                },{ error ->
                    Log.e("MainViewModel", "loadItemsByCategory: " + error.message)
                    _itemsByCategory.postValue(emptyList())
                })
        )
    }

    fun loadProductDetail(productId: Int) {
        compositeDisposable.add(
            repository.loadProductDetail(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success && response.result?.isNotEmpty() == true) {
                        // API trả về một danh sách chứa một sản phẩm, ta lấy phần tử đầu tiên
                        _productDetail.postValue(response.result[0])
                    } else {
                        // Nếu không tìm thấy sản phẩm, post giá trị null
                        Log.w("MainViewModel", "Product not found or unsuccessful response")
                        _productDetail.postValue(null)
                    }
                },{ error ->
                    Log.e("MainViewModel", "loadProductDetail error: " + error.message)
                    // Nếu có lỗi mạng, cũng post giá trị null
                    _productDetail.postValue(null)
                })
        )
    }

    // Dọn dẹp tất cả các yêu cầu mạng khi ViewModel bị hủy
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    private val _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>> = _allProducts

    fun loadAllProducts() {
        compositeDisposable.add(
            repository.loadAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        _allProducts.postValue(response.result ?: emptyList())
                    } else {
                        Log.w("MainViewModel", "loadAllProducts was not successful")
                        _allProducts.postValue(emptyList())
                    }
                }, { error ->
                    Log.e("MainViewModel", "loadAllProducts error: ${error.message}")
                    _allProducts.postValue(emptyList())
                })
        )
    }
}
