package com.example.coffeeshop.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.coffeeshop.R
import com.example.coffeeshop.ViewModel.ProfileViewModel
import com.example.coffeeshop.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)

        initControl()
        loadUserInfo()
        observeViewModel()
    }

    private fun initControl() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnLogout.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserInfo() {
        val userId = sharedPreferences.getInt("USER_ID", -1)
        if (userId != -1) {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.loadUserDetail(userId)
        } else {
            // Handle user not logged in
        }
    }

    private fun observeViewModel() {
        viewModel.userDetail.observe(this, Observer { user ->
            binding.progressBar.visibility = View.GONE
            if (user != null) {
                binding.txtUsername.text = user.username
                binding.txtEmail.text = user.email
                binding.txtEmailValue.text = user.email
                binding.txtPhoneValue.text = user.phone
                binding.txtUserIdValue.text = "#${user.id}"
            } else {
                Toast.makeText(this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.updateResult.observe(this, Observer { isSuccess ->
            binding.progressBar.visibility = View.GONE
            if (isSuccess) {
                Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
        val edtUsername = dialogView.findViewById<EditText>(R.id.edtUsername)
        val edtPhone = dialogView.findViewById<EditText>(R.id.edtPhone)

        // Pre-fill the fields with current data
        viewModel.userDetail.value?.let {
            edtUsername.setText(it.username)
            edtPhone.setText(it.phone)
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Lưu") { dialog, _ ->
                val newUsername = edtUsername.text.toString().trim()
                val newPhone = edtPhone.text.toString().trim()
                val userId = sharedPreferences.getInt("USER_ID", -1)

                if (newUsername.isNotEmpty() && newPhone.isNotEmpty() && userId != -1) {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.updateProfile(userId, newUsername, newPhone)
                } else {
                    Toast.makeText(this, "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
