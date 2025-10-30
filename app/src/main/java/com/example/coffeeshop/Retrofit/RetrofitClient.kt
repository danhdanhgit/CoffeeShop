package com.example.coffeeshop.Retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory

/**
 * Lớp RetrofitClient sử dụng mẫu Singleton để chỉ có một đối tượng Retrofit duy nhất
 * được tạo ra trong suốt vòng đời ứng dụng.
 */
object RetrofitClient {

    @Volatile
    private var instance: Retrofit? = null
    private var lastBaseUrl: String = ""

    /**
     * Lấy về instance của Retrofit.
     * Hàm này thread-safe (an toàn khi gọi từ nhiều luồng).
     *
     * Nó sẽ tạo mới một instance nếu được gọi với một `baseUrl` khác.
     */
    fun getInstance(baseUrl: String): Retrofit {
        // Nếu baseUrl thay đổi hoặc instance chưa được tạo
        if (baseUrl != lastBaseUrl || instance == null) {
            // Sử dụng synchronized để đảm bảo chỉ một luồng được phép vào và tạo instance
            synchronized(this) {
                // Kiểm tra lại điều kiện một lần nữa sau khi có được khóa
                if (baseUrl != lastBaseUrl || instance == null) {
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()

                    instance = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        // LƯU Ý: Bạn cần thêm thư viện cho RxJava3 để dòng này hoạt động
                        // implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build()
                    lastBaseUrl = baseUrl
                }
            }
        }
        // Trả về instance, !! để khẳng định nó không null ở đây
        return instance!!
    }
}
