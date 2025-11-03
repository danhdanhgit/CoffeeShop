package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Retrofit.ApiBanHang
import com.example.coffeeshop.Retrofit.RetrofitClient
import com.example.coffeeshop.databinding.ActivityRegisterBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    // --- SỬA LỖI: Lấy trực tiếp apiService từ RetrofitClient ---
    private val apiBanHang: ApiBanHang = RetrofitClient.apiService
    //Dùng để quản lý các lời gọi API bất đồng bộ
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Khởi tạo view binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set onClickListener cho nút đăng ký
        binding.btndangky.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.email.text.toString().trim()
        val username = binding.username.text.toString().trim()
        val password = binding.pass.text.toString()
        val confirmPassword = binding.repass.text.toString()
        val phone = binding.sodienthoai.text.toString().trim()

        // Kiểm tra input
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return
        }
    //3. Gọi Api và xử lý kết quả
        compositeDisposable.add(apiBanHang.registerUser(email, password, username, phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->

                if (response.success) {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Hiển thị thông báo lỗi cụ thể từ server
                    Toast.makeText(this, "Đăng ký thất bại: ${response.message}", Toast.LENGTH_LONG).show()
                }
            }, { throwable ->

                // Tạo thông báo lỗi chi tiết hơn cho người dùng
                val errorMessage = when (throwable) {
                    is HttpException -> {
                        // Cố gắng đọc nội dung lỗi từ server
                        val errorBody = throwable.response()?.errorBody()?.string() ?: "Không đọc được nội dung lỗi"
                        "Lỗi HTTP ${throwable.code()}: $errorBody"
                    }
                    is IOException -> "Lỗi kết nối mạng. Vui lòng kiểm tra lại Internet."
                    else -> "Đã có lỗi xảy ra: ${throwable.message}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            })
        )
    }

    override fun onDestroy() {
        //Hủy tất cả các subscription khi Activity bị hủy để tránh rò rỉ bộ nhớ
        compositeDisposable.clear()
        super.onDestroy()
    }

}