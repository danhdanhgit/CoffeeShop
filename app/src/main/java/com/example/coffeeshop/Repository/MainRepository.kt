package com.example.coffeeshop.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.LocalDataSource
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.CategoryModel
import com.example.coffeeshop.Domain.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(private val context: Context) {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val localDataSource = LocalDataSource(context)

    fun loadBanner(): MutableLiveData<MutableList<BannerModel>> {
        val listData = MutableLiveData<MutableList<BannerModel>>()
        
        // Load from Room first (offline data) - Single shot
        CoroutineScope(Dispatchers.Main).launch {
            val localBanners = localDataSource.getAllBannersOnce()
            if (localBanners.isNotEmpty()) {
                listData.value = ArrayList(localBanners)
            }
        }
        
        // Sync from Firebase
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BannerModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(BannerModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
                
                // Save to Room for offline access
                CoroutineScope(Dispatchers.IO).launch {
                    localDataSource.saveBanners(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Keep showing offline data
            }
        })
        
        return listData
    }

    fun loadCategory(): MutableLiveData<MutableList<CategoryModel>> {
        val listData = MutableLiveData<MutableList<CategoryModel>>()
        
        // Load from Room first - Single shot
        CoroutineScope(Dispatchers.Main).launch {
            val localCategories = localDataSource.getAllCategoriesOnce()
            if (localCategories.isNotEmpty()) {
                listData.value = ArrayList(localCategories)
            }
        }
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(CategoryModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
                
                // Save to Room
                CoroutineScope(Dispatchers.IO).launch {
                    localDataSource.saveCategories(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Keep showing offline data
            }
        })
        return listData
    }

    fun loadPopular(): MutableLiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        
        // Load from Room first - Single shot
        CoroutineScope(Dispatchers.Main).launch {
            val localProducts = localDataSource.getAllPopularProductsOnce()
            if (localProducts.isNotEmpty()) {
                listData.value = ArrayList(localProducts)
            }
        }
        
        val ref = firebaseDatabase.getReference("Popular")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
                
                // Save to Room
                CoroutineScope(Dispatchers.IO).launch {
                    localDataSource.savePopularProducts(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Keep showing offline data
            }
        })
        return listData
    }

    fun loadItemCategory(categoryId: String): LiveData<MutableList<ItemsModel>> {
        val itemLiveData = MutableLiveData<MutableList<ItemsModel>>()
        
        // Load from Room first - Single shot
        CoroutineScope(Dispatchers.Main).launch {
            val localProducts = localDataSource.getAllProductsByCategoryOnce(categoryId)
            if (localProducts.isNotEmpty()) {
                itemLiveData.value = ArrayList(localProducts)
            }
        }
        
        val ref = firebaseDatabase.getReference("Items")
        val query: Query = ref.orderByChild("categoryId").equalTo(categoryId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                // Keep showing offline data
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let { list.add(it) }
                }
                itemLiveData.value = list
                
                // Save to Room
                CoroutineScope(Dispatchers.IO).launch {
                    localDataSource.saveProductsByCategory(list, categoryId)
                }
            }
        })
        return itemLiveData
    }

}