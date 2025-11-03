package com.example.coffeeshop.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.CartAdapter
import com.example.coffeeshop.Helper.ManagmentCart
import com.example.coffeeshop.databinding.ActivityCartBinding


class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var managmentCart: ManagmentCart
    private var tax : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        calculateCart()
        setVariable()
        initCartList()
    }

    private fun initCartList() {
        binding.apply {
            rvCartView.layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            rvCartView.adapter = CartAdapter(
                managmentCart.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener{
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )

        }
    }

    private fun setVariable() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15000
        tax = Math.round((managmentCart.getTotalFee() * percentTax) *100)/ 100.0
        val total = Math.round((managmentCart.getTotalFee() + tax + delivery)*100)/ 100
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100)/ 100

        binding.apply {
            txtTotalFee.text = "${itemTotal} Đ"
            txtTax.text = "${tax} Đ"
            txtDelivery.text = "${delivery} Đ"
            txtTotal.text = "${total} Đ"
        }
    }
}