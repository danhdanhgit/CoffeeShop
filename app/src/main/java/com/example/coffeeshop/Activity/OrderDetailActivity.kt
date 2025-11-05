package com.example.coffeeshop.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.OrderItemAdapter
import com.example.coffeeshop.Data.Entity.Order
import com.example.coffeeshop.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val order = intent.getSerializableExtra("order") as? Order
        binding.btnBack.setOnClickListener { finish() }

        order?.let { o ->
            binding.txtOrderId.text = "Đơn hàng #${o.orderId}"
            binding.txtStatus.text = o.status
            binding.txtCustomerName.text = o.customerName
            binding.txtCustomerPhone.text = o.customerPhone
            binding.txtCustomerAddress.text = o.customerAddress
            binding.txtTotal.text = "${o.total.toInt()} Đ"

            binding.recyclerItems.layoutManager = LinearLayoutManager(this)
            binding.recyclerItems.adapter = OrderItemAdapter(o.items ?: emptyList())
        }
    }
}
