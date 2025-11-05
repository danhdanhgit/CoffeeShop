package com.example.coffeeshop.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.OrderAdapter
import com.example.coffeeshop.Data.Entity.Order
import com.example.coffeeshop.Helper.TinyDB
import com.example.coffeeshop.ViewModel.OrderViewModel
import com.example.coffeeshop.databinding.ActivityOrdersBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)

        initControl()
        loadOrders()
    }

    private fun initControl() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadOrders() {
        val userId = sharedPreferences.getInt("USER_ID", -1)

        if (userId == -1) {
            binding.txtEmpty.visibility = View.VISIBLE
            binding.recyclerViewOrders.visibility = View.GONE
            binding.txtEmpty.text = "Vui lòng đăng nhập để xem đơn hàng"
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.txtEmpty.visibility = View.GONE

        viewModel.loadOrders(userId)
        viewModel.orders.observe(this, Observer { orders ->
            binding.progressBar.visibility = View.GONE

            var displayOrders = orders
            if (displayOrders.isEmpty()) {
                // Fallback: lấy đơn hàng local nếu có
                val tiny = TinyDB(this)
                try {
                    val localOrder = tiny.getObject("LocalLastOrder", Order::class.java)
                    if (localOrder != null) {
                        displayOrders = listOf(localOrder)
                    }
                } catch (_: Exception) {}
            }

            if (displayOrders.isEmpty()) {
                binding.txtEmpty.visibility = View.VISIBLE
                binding.recyclerViewOrders.visibility = View.GONE
            } else {
                binding.txtEmpty.visibility = View.GONE
                binding.recyclerViewOrders.visibility = View.VISIBLE

                binding.recyclerViewOrders.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.recyclerViewOrders.adapter = OrderAdapter(displayOrders)
            }
        })
    }
}


