package com.example.coffeeshop.Retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Lớp RetrofitClient được tái cấu trúc thành một trung tâm API duy nhất.
 * Nó quản lý một địa chỉ BASE_URL và cung cấp một instance duy nhất của ApiBanHang.
 */
object RetrofitClient {

    // --- BƯỚC 1: Khai báo địa chỉ IP ở một nơi duy nhất ---
    // Giờ đây, bạn chỉ cần thay đổi địa chỉ IP ở đây.
    private const val BASE_URL = "http://192.168.1.4/coffeeshop/"
	//private const val BASE_URL = "http://192.168.88.166/coffeeshop/"

    // --- BƯỚC 2: Tạo ra một phiên bản ApiBanHang duy nhất và dùng chung ---
    val apiService: ApiBanHang by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiBanHang::class.java)
    }
}
