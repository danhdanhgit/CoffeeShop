package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.Retrofit.ApiBanHang
import com.example.coffeeshop.Retrofit.RetrofitClient
import com.example.coffeeshop.databinding.ActivityLoginBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiBanHang: ApiBanHang
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo apiBanHang
        apiBanHang = RetrofitClient.getInstance("http://192.168.88.166/coffeeshop/").create(ApiBanHang::class.java)

        initControl()
    }

    private fun initControl() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.txtresetpass.setOnClickListener {
            Toast.makeText(this, "Chức năng đặt lại mật khẩu chưa được triển khai", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doLogin() {
        val email = binding.txtemail.text.toString().trim()
        val pass = binding.txtpassword.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
            return
        }

        // Gọi API
        compositeDisposable.add(apiBanHang.login(email, pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                response ->
                Log.d("LoginActivity", "API Response: $response")

                if (response.success) {
                    // Lấy thông tin user từ kết quả trả về
                    val user = response.result.firstOrNull()
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                    // TODO: Chuyển sang màn hình chính và truyền dữ liệu người dùng qua Intent
                     val intent = Intent(this, MainActivity::class.java)
                     intent.putExtra("USER_NAME", user?.username)
                     startActivity(intent)
                     finish()

                } else {
                    Toast.makeText(this, "Đăng nhập thất bại: ${response.message}", Toast.LENGTH_LONG).show()
                }
            }, {
                throwable ->
                Log.e("LoginActivity", "API Call Failed", throwable)
                val errorMessage = when (throwable) {
                    is HttpException -> "Lỗi HTTP ${throwable.code()}: " + (throwable.response()?.errorBody()?.string() ?: "")
                    is IOException -> "Lỗi kết nối mạng."
                    else -> "Lỗi: ${throwable.message}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            })
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
