package com.example.coffeeshop.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.Entity.Category
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.Data.Entity.ProductResult
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.Repository.MainRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

// Sealed class để quản lý trạng thái kết quả tìm kiếm
sealed class SearchResultState {
    object Loading : SearchResultState()
    data class Success(val products: List<Product>) : SearchResultState()
    data class Error(val message: String) : SearchResultState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MainRepository(application)
    private val compositeDisposable = CompositeDisposable()

    // --- LiveData ---
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _productResultData = MutableLiveData<ProductResult?>()
    val productResultData: LiveData<ProductResult?> = _productResultData

    private val _itemsByCategory = MutableLiveData<List<Product>>()
    val itemsByCategory: LiveData<List<Product>> = _itemsByCategory

    private val _productDetail = MutableLiveData<Product?>()
    val productDetail: LiveData<Product?> = _productDetail

    private val _productVideos = MutableLiveData<List<String>>()
    val productVideos: LiveData<List<String>> = _productVideos

    private val _searchResults = MutableLiveData<SearchResultState>()
    val searchResults: LiveData<SearchResultState> = _searchResults

    private val _allProducts = MutableLiveData<List<Product>>()
    val allProducts: LiveData<List<Product>> = _allProducts

    // --- Categories ---
    fun loadCategories(): LiveData<List<Category>> {
        compositeDisposable.add(
            repository.loadCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val categories = response.result ?: emptyList()
                    if (response.success) {
                        _categories.postValue(categories)
                    } else {
                        Log.w("MainViewModel", "loadCategories was not successful")
                        _categories.postValue(emptyList())
                    }
                }, { error ->
                    Log.e("MainViewModel", "loadCategories error: ${error.message}")
                    _categories.postValue(emptyList())
                })
        )
        return categories
    }

    // --- Items by category ---
    fun loadItemsByCategory(categoryId: Int) {
        compositeDisposable.add(
            repository.loadItemsByCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    // ProductListResponse có result là List<Product> trực tiếp
                    val products = response.result ?: emptyList()
                    if (response.success) {
                        _itemsByCategory.postValue(products)
                    } else {
                        Log.w("MainViewModel", "loadItemsByCategory was not successful")
                        _itemsByCategory.postValue(emptyList())
                    }
                }, { error ->
                    Log.e("MainViewModel", "loadItemsByCategory error: ${error.message}")
                    _itemsByCategory.postValue(emptyList())
                })
        )
    }

    // --- Product Detail ---
    fun loadProductDetail(productId: Int) {
        compositeDisposable.add(
            repository.loadProductDetail(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    // ProductResponse có result là ProductResult với product, images, videos
                    val productResult = response.result
                    val firstProduct: Product? = productResult?.product
                    
                    if (response.success && firstProduct != null) {
                        _productDetail.postValue(firstProduct)
                        // Lấy images và videos từ ProductResult
                        _productDetail.postValue(response.result.product)
                        _productResultData.postValue(response.result)
                    } else {
                        Log.w("MainViewModel", "Product not found or unsuccessful response")
                        _productDetail.postValue(null)
                        _productResultData.postValue(null)
                    }
                }, { error ->
                    Log.e("MainViewModel", "loadProductDetail error: ${error.message}")
                    _productDetail.postValue(null)
                    _productResultData.postValue(null)
                })
        )
    }

    // --- Search ---
    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _searchResults.postValue(SearchResultState.Success(emptyList()))
            return
        }
        _searchResults.postValue(SearchResultState.Loading)
        compositeDisposable.add(
            repository.searchProducts(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    // ProductListResponse có result là List<Product> trực tiếp
                    val products = response.result ?: emptyList()
                    if (response.success) {
                        _searchResults.postValue(SearchResultState.Success(products))
                    } else {
                        _searchResults.postValue(SearchResultState.Error(response.message ?: "Lỗi không xác định"))
                    }
                }, { error ->
                    Log.e("MainViewModel", "searchProducts error: ${error.message}")
                    _searchResults.postValue(SearchResultState.Error(error.message ?: "Lỗi kết nối mạng"))
                })
        )
    }

//    // --- Load All Products ---
//    fun loadAllProducts(query: String) {
//        compositeDisposable.add(
//            repository.loadAllProducts()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                    val products = response.result?.product ?: emptyList()
//                    if (response.success) {
//                        _allProducts.postValue(products)
//                    } else {
//                        Log.w("MainViewModel", "loadAllProducts was not successful")
//                        _allProducts.postValue(emptyList())
//                    }
//                }, { error ->
//                    Log.e("MainViewModel", "loadAllProducts error: ${error.message}")
//                    _allProducts.postValue(emptyList())
//                })
//        )
//    }

    // --- Banner & Popular ---
    fun loadBanner(): LiveData<MutableList<BannerModel>> = repository.loadBanner()
    fun loadPopular(): LiveData<MutableList<ItemsModel>> = repository.loadPopular()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
