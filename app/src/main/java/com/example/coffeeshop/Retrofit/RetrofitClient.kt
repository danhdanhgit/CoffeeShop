package com.example.coffeeshop.Retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Lớp RetrofitClient được tái cấu trúc thành một trung tâm API duy nhất.
 * Nó quản lý một địa chỉ BASE_URL và cung cấp một instance duy nhất của ApiBanHang.
 */
object RetrofitClient {


    //private const val BASE_URL = "http://192.168.1.4/coffeeshop/"
	private const val BASE_URL = "http://192.168.88.169/coffeeshop/"


    val apiService: ApiBanHang by lazy {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        val gson = GsonBuilder()
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
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
