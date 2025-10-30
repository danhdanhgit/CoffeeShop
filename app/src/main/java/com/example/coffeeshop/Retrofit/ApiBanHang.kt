package com.example.coffeeshop.Retrofit

import com.example.coffeeshop.Data.Entity.CategoryResponse
import com.example.coffeeshop.Data.Entity.LoginResponse
import com.example.coffeeshop.Data.Entity.ProductResponse
import com.example.coffeeshop.Data.Entity.UserModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiBanHang {

    //Đăng ký tài khoản
    @POST("register.php")
    @FormUrlEncoded
    fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") username: String,
        @Field("phone") phone: String
    ): Observable<UserModel>

    //Đăng nhập
    @POST("login.php")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Observable<LoginResponse>

    //Lấy danh sách danh mục
    @GET("getcategory.php")
    fun getCategory(): Observable<CategoryResponse>

    //Lấy sản phẩm theo danh mục
    @POST("getItemsList.php")
    @FormUrlEncoded
    fun getItemsByCategory(
        @Field("category_id") categoryId: Int
    ): Observable<ProductResponse>
}
