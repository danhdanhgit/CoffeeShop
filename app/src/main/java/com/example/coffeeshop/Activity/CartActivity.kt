package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.CartAdapter
import com.example.coffeeshop.Data.Entity.Order
import com.example.coffeeshop.Data.Entity.OrderCreateResponse
import com.example.coffeeshop.Data.Entity.OrderItem
import com.example.coffeeshop.Helper.ManagmentCart
import com.example.coffeeshop.Helper.TinyDB
import com.example.coffeeshop.Retrofit.ApiBanHang
import com.example.coffeeshop.Retrofit.RetrofitClient
import com.example.coffeeshop.databinding.ActivityCartBinding
import com.example.coffeeshop.databinding.DialogOrderSuccessBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject


class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var managmentCart: ManagmentCart
    private var tax : Double = 0.0

    private val apiBanHang: ApiBanHang = RetrofitClient.apiService
    private val compositeDisposable = CompositeDisposable()

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

        binding.btnPayment.setOnClickListener {
            val name = binding.edtCustomerName.text?.toString()?.trim() ?: ""
            val phone = binding.edtCustomerPhone.text?.toString()?.trim() ?: ""
            val address = binding.edtCustomerAddress.text?.toString()?.trim() ?: ""

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Thiếu thông tin")
                    .setMessage("Vui lòng nhập đủ Tên, Số điện thoại và Địa chỉ để thanh toán.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            val percentTax = 0.02
            val delivery = 15000
            val cartItems = managmentCart.getListCart()
            val itemsTotal = cartItems.sumOf { it.price * it.numberInCart }
            val taxLocal = Math.round((itemsTotal * percentTax) * 100) / 100.0
            val totalLocal = Math.round((itemsTotal + taxLocal + delivery) * 100) / 100.0

            // items JSON
            val itemsArray = JSONArray()
            cartItems.forEach {
                val obj = JSONObject()
                obj.put("product_id", it.id)
                obj.put("qty", it.numberInCart)
                obj.put("size", it.size)
                obj.put("price", it.price)
                itemsArray.put(obj)
            }

            compositeDisposable.add(
                apiBanHang.createOrder(
                    userId = getSharedPreferences("USER_PREFS", MODE_PRIVATE).getInt("USER_ID", 0),
                    customerName = name,
                    customerPhone = phone,
                    customerAddress = address,
                    total = totalLocal,
                    itemsJson = itemsArray.toString()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ _: OrderCreateResponse ->
                        showSuccessDialogAndGoHome()
                    }, { _ ->
                        // fallback nếu lỗi
                        saveLocalOrder(name, phone, address, totalLocal, cartItems)
                        showSuccessDialogAndGoHome()
                    })
            )
        }
    }

    private fun saveLocalOrder(name: String, phone: String, address: String, totalLocal: Double, items: ArrayList<com.example.coffeeshop.Data.Entity.Product>) {
        val orderItems = items.map {
            OrderItem(
                orderItemId = 0,
                orderId = 0,
                productId = it.id,
                quantity = it.numberInCart,
                size = it.size,
                price = it.price,
                productTitle = it.title,
                productUrl = it.picUrl
            )
        }
        val localOrder = Order(
            orderId = (System.currentTimeMillis() / 1000L).toInt(),
            userId = 0,
            customerName = name,
            customerPhone = phone,
            customerAddress = address,
            total = totalLocal.toFloat(),
            status = "pending",
            createdAt = "",
            items = orderItems
        )
        val tiny = TinyDB(this)
        tiny.putObject("LocalLastOrder", localOrder)
    }

    private fun showSuccessDialogAndGoHome() {
        AlertDialog.Builder(this)
        .setTitle("Thành công")
        .setMessage("Đơn hàng của bạn đã được đặt thành công!")
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            managmentCart.clearCart()

            // Tạo Intent để quay về màn hình chính
            val intent = Intent(this, MainActivity::class.java)
            // Thêm cờ để xóa các Activity phía trên và không tạo Activity mới nếu đã có
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Đóng CartActivity hiện tại
        }
        .setCancelable(false) // Không cho phép đóng dialog bằng cách nhấn ra ngoài
        .show()
    }


    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15000
        tax = Math.round((managmentCart.getTotalFee() * percentTax) *100)/ 100.0
        val total = Math.round((managmentCart.getTotalFee() + tax + delivery)*100)/ 100
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100)/ 100

        binding.apply {
            txtTotalFee.text = "${itemTotal} VND"
            txtTax.text = "${tax} VND"
            txtDelivery.text = "${delivery} VND"
            txtTotal.text = "${total} VND"
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
