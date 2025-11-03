package com.example.coffeeshop.Repository

import android.content.Context
import com.example.coffeeshop.Data.Entity.CategoryResponse
import com.example.coffeeshop.Data.Entity.OrderResponse
import com.example.coffeeshop.Data.Entity.ProductResponse
import com.example.coffeeshop.Data.Entity.UserDetailResponse
import com.example.coffeeshop.Domain.BannerModel
import com.example.coffeeshop.Domain.ItemsModel
import com.example.coffeeshop.Retrofit.ApiBanHang
import com.example.coffeeshop.Retrofit.RetrofitClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable

class MainRepository(private val context: Context) {
    private val apiBanHang: ApiBanHang by lazy {
        RetrofitClient.getInstance("http://192.168.88.166/coffeeshop/").create(ApiBanHang::class.java)
    }

    // Trả về trực tiếp Observable từ Retrofit
    fun loadCategory(): Observable<CategoryResponse> {
        return apiBanHang.getCategory()
    }

    // Trả về trực tiếp Observable từ Retrofit
    fun loadItemsByCategory(categoryId: Int): Observable<ProductResponse> {
        return apiBanHang.getItemsByCategory(categoryId)
    }

    // Trả về trực tiếp Observable từ Retrofit
    fun loadProductDetail(productId: Int): Observable<ProductResponse> {
        return apiBanHang.getProductDetail(productId)
    }

    // Lấy thông tin chi tiết user
    fun loadUserDetail(userId: Int): Observable<UserDetailResponse> {
        return apiBanHang.getUserDetail(userId)
    }

    // Lấy danh sách đơn hàng
    fun loadOrders(userId: Int): Observable<OrderResponse> {
        return apiBanHang.getOrders(userId)
    }

    // Lấy tất cả sản phẩm (cho tìm kiếm)
    fun loadAllProducts(): Observable<ProductResponse> {
        return apiBanHang.getAllProducts()
    }

    // ----- Các hàm Firebase được giữ lại để không làm hỏng các phần khác của ứng dụng -----
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    fun loadBanner(): androidx.lifecycle.MutableLiveData<MutableList<BannerModel>> {
        val listData = androidx.lifecycle.MutableLiveData<MutableList<BannerModel>>()

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

     fun loadPopular(): androidx.lifecycle.MutableLiveData<MutableList<ItemsModel>> {
        val listData = androidx.lifecycle.MutableLiveData<MutableList<ItemsModel>>()

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
}
