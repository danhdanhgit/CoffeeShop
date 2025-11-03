package com.example.coffeeshop.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.coffeeshop.ViewModel.ProfileViewModel
import com.example.coffeeshop.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)

        initControl()
        loadUserInfo()
    }

    private fun initControl() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(this, "Chức năng chỉnh sửa thông tin đang được phát triển", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            // Xóa thông tin user đã lưu
            sharedPreferences.edit().clear().apply()
            
            // Chuyển về màn hình đăng nhập
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserInfo() {
        val userId = sharedPreferences.getInt("USER_ID", -1)
        
        if (userId == -1) {
            // Nếu chưa có user_id, thử lấy từ intent hoặc hiển thị thông tin mặc định
            val username = sharedPreferences.getString("USER_NAME", "Người dùng") ?: "Người dùng"
            binding.txtUsername.text = username
            binding.txtEmail.text = "Chưa có thông tin"
            binding.txtEmailValue.text = "Chưa có thông tin"
            binding.txtPhoneValue.text = "Chưa có thông tin"
            binding.txtUserIdValue.text = "#---"
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        viewModel.loadUserDetail(userId)
        viewModel.userDetail.observe(this, Observer { user ->
            binding.progressBar.visibility = View.GONE

            if (user != null) {
                binding.txtUsername.text = user.username
                binding.txtEmail.text = user.email
                binding.txtEmailValue.text = user.email
                binding.txtPhoneValue.text = user.phone
                binding.txtUserIdValue.text = "#${user.id}"
            } else {
                // Hiển thị thông tin từ SharedPreferences nếu API fail
                val username = sharedPreferences.getString("USER_NAME", "Người dùng") ?: "Người dùng"
                binding.txtUsername.text = username
                binding.txtEmail.text = "Không thể tải thông tin"
                binding.txtEmailValue.text = "Không thể tải thông tin"
                binding.txtPhoneValue.text = "Không thể tải thông tin"
                binding.txtUserIdValue.text = "#---"
            }
        })
    }
}


