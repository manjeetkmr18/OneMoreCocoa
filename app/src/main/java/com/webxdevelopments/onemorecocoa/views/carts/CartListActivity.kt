package com.webxdevelopments.onemorecocoa.views.carts

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityCartListBinding
import com.webxdevelopments.onemorecocoa.views.carts.model.CartFullListModel
import com.webxdevelopments.onemorecocoa.views.carts.model.CartListModel
import com.webxdevelopments.onemorecocoa.views.checkout.CheckoutActivity
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseCartDB

class CartListActivity : BaseBindingActivity<ActivityCartListBinding>() {
    private var TAG = CartListActivity::class.java.simpleName.toString()
    private var  cartFullList = ArrayList<CartFullListModel>()
    private lateinit var cartListAdapter : CartListAdapter
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

        if(CommonUtils.isConnectingToInternet(this) == true){
            CommonUtils.showLoader(this)
            val userId = Firebase.auth.currentUser!!.uid
            FirebaseCartDB().getProductsFromCart(this, userId, object : FirebaseCartDB.GetProductsFromCartCallback{
                override fun getProductsFromCartSuccess(productsList: ArrayList<CartFullListModel>) {
                    CommonUtils.hideLoader()
                    cartFullList.clear()
                    cartFullList.addAll(productsList)
                    setCartListAdapter()
                    if(cartFullList.size>0) {
                        noData(false)
                    }else{
                        noData(true)
                    }
                }
                override fun getProductsFromCartFailure(msg: String) {
                    CommonUtils.hideLoader()
                    noData(true)
                }
            })
        }
        else{
            CommonUtils.showToast(this, resources.getString(R.string.no_internet_connection))
        }
    }

    private fun noData(isVisi:Boolean){
        if(isVisi){
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerProductList.visibility = View.GONE
            binding.btnCheckout.visibility = View.GONE
        }else{
            binding.tvNoData.visibility = View.GONE
            binding.recyclerProductList.visibility = View.VISIBLE
            binding.btnCheckout.visibility = View.VISIBLE
        }
    }

    private fun addCartQuantity(productId:String, quantity:Int , position:Int){
        if (CommonUtils.isConnectingToInternet(this) == true){
            val userId = Firebase.auth.currentUser!!.uid
            val cartModel = CartListModel(productId.toString(), quantity)
            FirebaseCartDB().addDataToCart(
                this,
                userId,
                cartModel,
                object : FirebaseCartDB.AddProductToCartCallback {
                    override fun addProductToCartSuccess(cartModel: CartListModel) {
                        Log.e(TAG, "addProductToCartSuccess---cartModel")
                        cartFullList[position].cartModel.cartProductId = productId
                        cartListAdapter.notifyDataSetChanged()
                    }

                    override fun addProductToCartFailure(msg: String) {
                        Log.e(TAG, "addProductToCartFailure---cartModel")
                    }

                })
        }
        else{
            CommonUtils.showToast(this, resources.getString(R.string.no_internet_connection))
        }
    }

    private fun removeFromCart(cartId:String, position:Int){
        if (CommonUtils.isConnectingToInternet(this) == true) {
            CommonUtils.showLoader(this)
            val userId = Firebase.auth.currentUser!!.uid
            FirebaseCartDB().removeProductFromCart(
                activity = this,
                userId = userId,
                cartId,
                object : FirebaseCartDB.RemoveProductsFromCartCallback {
                    override fun onRemoveFromCartSuccess(isSuccess: Boolean, msg: String) {
                        CommonUtils.hideLoader()
                        if (isSuccess) {
                            if(cartFullList.size>position){
                                cartFullList.removeAt(position)
                                cartListAdapter.notifyDataSetChanged()
                            }
                        } else {
                            CommonUtils.showToast(this@CartListActivity, msg)
                        }
                    }

                })
        }
        else{
            CommonUtils.showToast(this, resources.getString(R.string.no_internet_connection))
        }
    }
    private fun setCartListAdapter() {
        binding.recyclerProductList.layoutManager = LinearLayoutManager(this)
        cartListAdapter = CartListAdapter(
            this,
            cartFullList,
            object : CartListAdapter.ProductListCallback {
                override fun onItemClick(position: Int, cardView: MaterialCardView) {
                   /* val intent =
                        Intent(this@CartListActivity, ProductDetailsActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this@CartListActivity,
                        cardView,
                        "product_details"
                    )
                    startActivity(intent, options.toBundle())*/
                }

                override fun onCartQuantityChange(position: Int, count: Int) {
                    val productId = cartFullList[position].cartModel.cartProductId
                    addCartQuantity(productId, count, position)

                }

                override fun onCartDelete(position: Int) {
                    val productId = cartFullList[position].cartModel.cartProductId
                    removeFromCart(productId, position)
                }
                //removeFromCart
            })
        binding.recyclerProductList.adapter = cartListAdapter
    }

}