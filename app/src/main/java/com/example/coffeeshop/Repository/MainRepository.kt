package com.example.coffeeshop.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Data.Entity.Category
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.Retrofit.ApiBanHang
import com.example.coffeeshop.Retrofit.RetrofitClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainRepository(private val context: Context) {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val apiBanHang: ApiBanHang by lazy {
        RetrofitClient.getInstance("http://192.168.88.166/coffeeshop/").create(ApiBanHang::class.java)
    }
    private val compositeDisposable = CompositeDisposable()

    fun loadBanner(): MutableLiveData<MutableList<BannerModel>> {
        val listData = MutableLiveData<MutableList<BannerModel>>()

        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BannerModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(BannerModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        
        return listData
    }

    fun loadCategory(): MutableLiveData<MutableList<Category>> {
        val listData = MutableLiveData<MutableList<Category>>()

        compositeDisposable.add(apiBanHang.getCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                response ->
                if (response.success){
                    response.result?.let { listData.postValue(it.toMutableList()) }
                }
            },{
                error -> Log.e("MainRepository", "loadCategory: " + error.message)
            })
        )

        return listData
    }

    fun loadPopular(): MutableLiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()

        val ref = firebaseDatabase.getReference("Popular")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return listData
    }

    fun loadItemsByCategory(categoryId: Int): LiveData<MutableList<Product>> {
        val listData = MutableLiveData<MutableList<Product>>()

        compositeDisposable.add(apiBanHang.getItemsByCategory(categoryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                // Log the raw JSON response to see what the server is sending
                Log.d("API_RESPONSE", "ItemsByCategory Response: ${Gson().toJson(response)}")

                if (response.success){
                    response.result?.let {
                        listData.postValue(it.toMutableList())
                        if (it.isEmpty()) {
                            Log.w("API_RESPONSE", "API call successful but result list is empty.")
                        }
                    }
                } else {
                    Log.e("API_RESPONSE", "API call failed: ${response.message}")
                    listData.postValue(mutableListOf()) // Return empty list on failure
                }
            },{ error ->
                // Log the full error
                Log.e("API_ERROR", "loadItemsByCategory error", error)
                listData.postValue(mutableListOf()) // Return empty list on error
            })
        )

        return listData
    }
}
