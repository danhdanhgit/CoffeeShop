package com.example.coffeeshop.Activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.coffeeshop.Adapter.MediaAdapter
import com.example.coffeeshop.Data.Entity.Product
import com.example.coffeeshop.Data.Entity.ProductResult
import com.example.coffeeshop.Helper.ManagmentCart
import com.example.coffeeshop.Helper.ManagmentFavorites
import com.example.coffeeshop.R
import com.example.coffeeshop.ViewModel.MainViewModel
import com.example.coffeeshop.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var managmentCart: ManagmentCart
    private lateinit var managmentFavorites: ManagmentFavorites
    private lateinit var currentUserId: String
    private lateinit var currentProduct: Product
    private var mediaAdapter: MediaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)
        managmentFavorites = ManagmentFavorites(this)
        val sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val userIdValue: Any? = sharedPreferences.all["USER_ID"]
        currentUserId = userIdValue?.toString() ?: ""
        getIntentDataAndLoad()
        initSizeList()
        initFavoriteButton()
        initRecyclerView()
    }

    private fun getIntentDataAndLoad() {
        // Lấy ID sản phẩm được gửi từ Activity trước
        val productId = intent.getIntExtra("product_id", -1)
        if (productId == -1) {
            finish()
            return
        }

        // Hiển thị thanh tiến trình và gọi ViewModel để tải chi tiết sản phẩm
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadProductDetail(productId)

        // Lắng nghe dữ liệu từ ViewModel
        viewModel.productDetail.observe(this, Observer { product ->
            binding.progressBar.visibility = View.GONE
            if (product != null) {
                product.numberInCart = 1
                setupUI(product)
            } else {
                finish()
            }
        })

        // Lắng nghe images và videos từ ProductResult để cập nhật recyclerView
        viewModel.productResultData.observe(this, Observer { productResult ->
            //khởi taọ MediaAdapter với toàn bộ ProductResult
            val mediaAdapter = MediaAdapter(this, productResult)
            binding.recyclerMedia.adapter = mediaAdapter
        })
    }

    private fun setupUI(item: Product) {
        currentProduct = item
        binding.apply {
//            // Tải ảnh sản phẩm bằng Glide
//            Glide.with(this@DetailActivity)
//                .load(item.picUrl)
//                .into(picMain)

            // Cập nhật các thông tin sản phẩm
            txtTitle.text = item.title
            txtDescription.text = item.description
            txtExtra.text = item.extra
            txtPrice.text = "${item.price} VND"
            txtRating.text = item.rating.toString()
            txtNumberItem.text = item.numberInCart.toString()


            
            // Chọn size "Nhỏ" làm mặc định
            updateSizeSelection("Nhỏ")
            updateFavoriteButton(item)

            // Xử lý sự kiện thêm vào giỏ hàng
            btnAddToCart.setOnClickListener {
                managmentCart.insertItems(item)
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            // Xử lý sự kiện nút quay lại
            btnBack.setOnClickListener {
                finish()
            }

            // Xử lý sự kiện nút tăng số lượng
            btnPlus.setOnClickListener {
                item.numberInCart++
                txtNumberItem.text = item.numberInCart.toString()
            }

            // Xử lý sự kiện nút giảm số lượng
            btnMinus.setOnClickListener {
                // Chỉ giảm nếu số lượng lớn hơn 1
                if (item.numberInCart > 1) {
                    item.numberInCart--
                    txtNumberItem.text = item.numberInCart.toString()
                }
            }
        }
    }

    private fun initFavoriteButton() {
        binding.btnFav.setOnClickListener {
            if (currentUserId.isNotBlank()) {
                managmentFavorites.toggleFavorite(currentProduct, currentUserId)
                
                updateFavoriteButton(currentProduct)
            } else {
                Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFavoriteButton(product: Product) {
        if (currentUserId.isNotBlank() && managmentFavorites.isFavorite(currentProduct.id, currentUserId)) {
            binding.btnFav.setColorFilter(
                ContextCompat.getColor(this, R.color.orange),
                PorterDuff.Mode.SRC_IN)
        }else {
            binding.btnFav.clearColorFilter()
        }
    }

    private fun initSizeList() {
        binding.apply {
            btnSmall.setOnClickListener { updateSizeSelection("Nhỏ") }
            btnMedium.setOnClickListener { updateSizeSelection("Vừa") }
            btnLarge.setOnClickListener { updateSizeSelection("Lớn") }
        }
    }

    private fun updateSizeSelection(size: String) {
        currentProduct?.size = size
        binding.btnSmall.setBackgroundResource(if (size == "Nhỏ") R.drawable.stroke_brown_bg else 0)
        binding.btnMedium.setBackgroundResource(if (size == "Vừa") R.drawable.stroke_brown_bg else 0)
        binding.btnLarge.setBackgroundResource(if (size == "Lớn") R.drawable.stroke_brown_bg else 0)
    }

    private fun initRecyclerView() {
        //Thiết lập RecyclerView cho media
        binding.recyclerMedia.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //Tạo 1 snap helper
        val snapHelper: SnapHelper = PagerSnapHelper()

        //Gán snapHelper vào recyclerView
        snapHelper.attachToRecyclerView(binding.recyclerMedia)
    }

}
