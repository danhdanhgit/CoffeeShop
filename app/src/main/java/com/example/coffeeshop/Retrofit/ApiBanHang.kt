package com.example.coffeeshop.Retrofit

import com.example.coffeeshop.Data.Entity.CategoryResponse
import com.example.coffeeshop.Data.Entity.LoginResponse
import com.example.coffeeshop.Data.Entity.OrderCreateResponse
import com.example.coffeeshop.Data.Entity.OrderResponse
import com.example.coffeeshop.Data.Entity.ProductResponse
import com.example.coffeeshop.Data.Entity.UpdateProfileResponse
import com.example.coffeeshop.Data.Entity.UserDetailResponse
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

	//Lấy chi tiết sản phẩm
	@POST("getproduct.php")
	@FormUrlEncoded
	fun getProductDetail(
		@Field("product_id") productId: Int
	): Observable<ProductResponse>

	//Lấy thông tin chi tiết user
	@POST("getuser.php")
	@FormUrlEncoded
	fun getUserDetail(
		@Field("user_id") userId: Int
	): Observable<UserDetailResponse>

	//Lấy danh sách đơn hàng của user
	@POST("getorders.php")
	@FormUrlEncoded
	fun getOrders(
		@Field("user_id") userId: Int
	): Observable<OrderResponse>

	//Lấy tất cả sản phẩm (cho chức năng tìm kiếm)
	@GET("getallproducts.php")
	fun getAllProducts(): Observable<ProductResponse>

	//Tạo đơn hàng
	@POST("createorder.php")
	@FormUrlEncoded
	fun createOrder(
		@Field("user_id") userId: Int,
		@Field("customer_name") customerName: String,
		@Field("customer_phone") customerPhone: String,
		@Field("customer_address") customerAddress: String,
		@Field("total") total: Double,
		@Field("items") itemsJson: String
	): Observable<OrderCreateResponse>

	//Tìm kiếm
	@POST("search.php")
	@FormUrlEncoded
	fun search(
		@Field("search") query: String
	): Observable<ProductResponse>

	//Cập nhật thông tin user
	@POST("update_profile.php")
	@FormUrlEncoded
	fun updateProfile(
		@Field("user_id") userId: Int,
		@Field("username") username: String,
		@Field("phone") phone: String
	): Observable<UpdateProfileResponse>
}
