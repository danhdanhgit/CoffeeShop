package com.example.coffeeshop.Activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.coffeeshop.Data.Entity.Product
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
    private var currentProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)
        managmentFavorites = ManagmentFavorites(this)

        getIntentDataAndLoad()
        initSizeList()
        initFavoriteButton()
    }

    private fun getIntentDataAndLoad() {
        // Lấy ID sản phẩm được gửi từ Activity trước
        val productId = intent.getIntExtra("product_id", -1)
        if (productId == -1) {
            // Nếu không có ID, kết thúc activity để tránh lỗi
            finish()
            return
        }

        // Hiển thị thanh tiến trình và gọi ViewModel để tải chi tiết sản phẩm
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadProductDetail(productId)

        // Lắng nghe dữ liệu từ ViewModel
        viewModel.productDetail.observe(this, Observer { product ->
            // Ẩn thanh tiến trình khi có dữ liệu trả về
            binding.progressBar.visibility = View.GONE

            // product là một đối tượng Product? (có thể null)
            if (product != null) {
                // Khởi tạo số lượng ban đầu là 1
                product.numberInCart = 1
                // Cập nhật giao diện với dữ liệu sản phẩm
                setupUI(product)
            } else {
                // Nếu không tìm thấy sản phẩm, kết thúc activity
                // Bạn cũng có thể hiển thị một thông báo lỗi ở đây
                finish()
            }
        })
    }

    private fun setupUI(item: Product) {
        currentProduct = item
        binding.apply {
            // Tải ảnh sản phẩm bằng Glide
            Glide.with(this@DetailActivity)
                .load(item.picUrl) // Sửa lại thành item.url cho đúng với Product model
                .into(picMain)

            // Cập nhật các thông tin sản phẩm
            txtTitle.text = item.title
            txtDescription.text = item.description
            txtPrice.text = "${item.price} Đ"
            txtRating.text = item.rating.toString()
            txtNumberItem.text = item.numberInCart.toString()

            // Cập nhật trạng thái favorite button
            updateFavoriteButton(item)

            // Xử lý sự kiện thêm vào giỏ hàng
            btnAddToCart.setOnClickListener {
                managmentCart.insertItems(item)
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
            currentProduct?.let { product ->
                managmentFavorites.toggleFavorite(product)
                updateFavoriteButton(product)
            }
        }
    }

    private fun updateFavoriteButton(product: Product) {
        val isFavorite = managmentFavorites.isFavorite(product.id)
        if (isFavorite) {
            // Đổi màu đỏ
            binding.btnFav.setColorFilter(
                ContextCompat.getColor(this, R.color.orange),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            // Màu trắng
            binding.btnFav.clearColorFilter()
        }
    }

    private fun initSizeList() {
        binding.apply {
            btnSmall.setOnClickListener {
                btnSmall.setBackgroundResource(R.drawable.stroke_brown_bg)
                btnMedium.setBackgroundResource(0)
                btnLarge.setBackgroundResource(0)
            }
            btnMedium.setOnClickListener {
                btnSmall.setBackgroundResource(0)
                btnMedium.setBackgroundResource(R.drawable.stroke_brown_bg)
                btnLarge.setBackgroundResource(0)
            }
            btnLarge.setOnClickListener {
                btnSmall.setBackgroundResource(0)
                btnMedium.setBackgroundResource(0)
                btnLarge.setBackgroundResource(R.drawable.stroke_brown_bg)
            }
        }
    }
}
