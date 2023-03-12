package com.webxdevelopments.onemorecocoa.views.carts

import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.databinding.ActivityCartListBinding
import com.webxdevelopments.onemorecocoa.views.checkout.CheckoutActivity
import com.webxdevelopments.onemorecocoa.views.products.ProductDetailsActivity
import com.google.android.material.card.MaterialCardView
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity

class CartListActivity : BaseBindingActivity<ActivityCartListBinding>() {
    private var TAG = CartListActivity::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.activity_cart_list
    }

    override fun initUI() {
        binding.includeLayoutBack.tvLayoutTitle.visibility = View.VISIBLE
        binding.includeLayoutBack.tvLayoutTitle.text = "Cart"
        binding.includeLayoutBack.cardBack.setOnClickListener {
            finish()
        }
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        setCartListAdapter()
    }

    private fun setCartListAdapter() {
        binding.recyclerProductList.layoutManager = LinearLayoutManager(this)
        var cartListAdapter = CartListAdapter(
                this,
                ArrayList<String>(),
                object : CartListAdapter.ProductListCallback {
                    override fun onItemClick(position: Int, cardView: MaterialCardView) {
                        val intent =
                            Intent(this@CartListActivity, ProductDetailsActivity::class.java)
                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            this@CartListActivity,
                            cardView,
                            "product_details"
                        )
                        startActivity(intent, options.toBundle())
                    }
                })
        binding.recyclerProductList.adapter = cartListAdapter
    }
}